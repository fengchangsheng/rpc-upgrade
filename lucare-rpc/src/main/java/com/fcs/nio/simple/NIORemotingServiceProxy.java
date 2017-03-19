package com.fcs.nio.simple;

import com.fcs.nio.util.ByteUtil;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by Lucare.Feng on 2017/3/17.
 */
public class NIORemotingServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",1234);
        SocketChannel socketChannel = SocketChannel.open(inetSocketAddress);
        socketChannel.configureBlocking(false);
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        String methodName = method.getName();
        Class<?>[] paramType = method.getParameterTypes();
        SendObj sendObj = new SendObj(methodName, paramType, args);
        socketChannel.write(ByteBuffer.wrap(ByteUtil.getBytes(sendObj)));
        Object object ;
        while (true) {
            byteBuffer.clear();
            int readBytes = socketChannel.read(byteBuffer);
            if (readBytes > 0) {
//                byteBuffer.flip();
                object = ByteUtil.getObject(byteBuffer.array());
                System.out.println("Client: data = " + object);
                socketChannel.close();
                break;
            }
        }
        return object;
    }

}
