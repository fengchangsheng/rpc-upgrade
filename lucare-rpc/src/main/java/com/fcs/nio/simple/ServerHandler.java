package com.fcs.nio.simple;

import com.fcs.nio.util.ByteUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by Lucare.Feng on 2017/3/18.
 */
public class ServerHandler implements Handler {

    private Object service;

    public ServerHandler(Object object) {
        this.service = object;
    }

    @Override
    public void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        System.out.println("Server: accept client socket " + socketChannel);
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(), SelectionKey.OP_READ);
    }

    @Override
    public void handleRead(SelectionKey key) throws IOException, ClassNotFoundException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        SocketChannel socketChannel = (SocketChannel) key.channel();
        final Class clazz = service.getClass();
        while (true) {
            int readBytes = socketChannel.read(byteBuffer);
            if (readBytes > 0) {
                SendObj sendObj = (SendObj) ByteUtil.getObject(byteBuffer.array());
                System.out.println(sendObj.getMethodName());
                Object result = null;
                try {
                    Method method = clazz.getMethod(sendObj.getMethodName(), sendObj.getParamTypes());
                    result = method.invoke(service, sendObj.getAgrs());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                byteBuffer.flip();
                socketChannel.write(ByteBuffer.wrap(ByteUtil.getBytes(result)));
                break;
            }
        }
        socketChannel.close();
    }

    @Override
    public void handleWrite(SelectionKey key) throws IOException {
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
        byteBuffer.flip();
        SocketChannel socketChannel = (SocketChannel)key.channel();
        socketChannel.write(byteBuffer);
        if(byteBuffer.hasRemaining()) {
            key.interestOps(SelectionKey.OP_READ);
        }
        byteBuffer.compact();
    }


}
