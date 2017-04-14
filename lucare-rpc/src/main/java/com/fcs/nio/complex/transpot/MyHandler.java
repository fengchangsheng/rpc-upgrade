package com.fcs.nio.complex.transpot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fcs.bio.complex.server.contexts.AppContext;
import com.fcs.bio.complex.server.contexts.AppContextManager;
import com.fcs.bio.complex.server.contexts.ServiceSkeleton;
import com.fcs.nio.complex.SendDTO;
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
public class MyHandler implements Runnable {

    final SocketChannel socket;
    final SelectionKey sk;
    ByteBuffer input = ByteBuffer.allocate(512);
    ByteBuffer output = ByteBuffer.allocate(512);
    static final int READING = 0, SENDING = 1;
    int state = READING;

    public MyHandler(Selector selector, SocketChannel socketChannel) throws IOException {
        socket = socketChannel;
        socketChannel.configureBlocking(false);
        sk = socket.register(selector, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();

    }

    @Override
    public void run() {
        if (state == READING) {
            try {
                read();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (state == SENDING) {
            send();
        }
    }

    private void read() throws IOException, ClassNotFoundException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        SocketChannel socketChannel = (SocketChannel) sk.channel();

        while (true) {
            int readBytes = socketChannel.read(byteBuffer);
            if (readBytes != -1) {
                byte[] in = byteBuffer.array();
                Object[] params = JSON.parseObject(in, Object[].class);
                SendDTO sendDTO = arrayToObjects(params);
                Object result = process(sendDTO);
                byteBuffer.flip();
                byte[] data = JSON.toJSONBytes(result, SerializerFeature.WriteClassName, SerializerFeature.WriteDateUseDateFormat);
                System.out.println(data.length);
                socketChannel.write(ByteBuffer.wrap(data));
                break;
            }
        }
//        state = SENDING;
//        sk.interestOps(SelectionKey.OP_WRITE);
        socketChannel.close();
    }

    private Object process(SendDTO sendDTO) {
        AppContext appContext = AppContextManager.getAppContextImpl("test");
        ServiceSkeleton serviceSkeleton = appContext.getServiceSkeleton(sendDTO.getServiceName());
        Object service = serviceSkeleton.getService();
        final Class clazz = service.getClass();
        Object result = null;
        try {
            Method method = clazz.getMethod(sendDTO.getMethodName(), sendDTO.getParamTypes());
            result = method.invoke(service, sendDTO.getAgrs());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return result;
    }

    private SendDTO arrayToObjects(Object[] params) {
        String serviceName = (String) params[0];
        String methodName = (String) params[1];
        JSONArray typeArray = (JSONArray) params[2];
        Class<?>[] paramTypes = new Class<?>[typeArray.size()];
        paramTypes[0] = java.lang.String.class;
        for (int index = 0; index < typeArray.size(); index++) {
            paramTypes[index] = typeArray.get(index).getClass();
        }
        JSONArray array = (JSONArray) params[3];
        Object[] args = new Object[array.size()];
        for (int index = 0; index < array.size(); index++) {
            args[index] = array.get(index);
        }
        return new SendDTO(serviceName, methodName, paramTypes, args);
    }

    private void send() {
        System.out.println("this is send ");
    }

}
