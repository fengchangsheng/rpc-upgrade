package com.fcs.nio.complex.transpot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fcs.bio.complex.remoting.RPCHolder;
import com.fcs.bio.complex.server.contexts.AppContext;
import com.fcs.bio.complex.server.contexts.AppContextManager;
import com.fcs.bio.complex.server.contexts.ServiceSkeleton;
import com.fcs.nio.complex.codec.CodecHelper;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Created by Lucare.Feng on 2017/4/5.
 */
public class Handler implements Runnable {

    final SocketChannel socket;
    final SelectionKey sk;
    static final int READING = 0, SENDING = 1;
    int state = READING;
    byte[] data = null;

    public Handler(Selector selector, SocketChannel socketChannel) throws IOException {
        socket = socketChannel;
        socketChannel.configureBlocking(false);
        sk = socket.register(selector, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();

    }

    @Override
    public void run() {
        try {
            if (state == READING) {
                read();
            } else if (state == SENDING) {
                send();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void read() throws IOException, ClassNotFoundException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (true) {
            int readBytes = socket.read(byteBuffer);
            if (readBytes != -1) {
                byteBuffer.flip();
                RPCHolder holder = CodecHelper.decodeRequest(byteBuffer);
                Object object = process(holder);
                data = JSON.toJSONBytes(object, SerializerFeature.WriteClassName, SerializerFeature.WriteDateUseDateFormat);
                break;
            }
        }
        state = SENDING;
        sk.interestOps(SelectionKey.OP_WRITE);
        sk.selector().wakeup();
    }

    private Object process(RPCHolder holder) {
        AppContext appContext = AppContextManager.getAppContextImpl("test");
        ServiceSkeleton serviceSkeleton = appContext.getServiceSkeleton(holder.getServiceName());
        Object service = serviceSkeleton.getService();
        final Class clazz = service.getClass();
        Object result = null;
        try {
            Method method = clazz.getMethod(holder.getMethodName(), holder.getParameterTypes());
            result = method.invoke(service, holder.getArgs());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void send() throws IOException {
        socket.write(ByteBuffer.wrap(data));
//        sk.cancel();
    }

}
