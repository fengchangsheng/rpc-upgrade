package com.fcs.nio.simple;


import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Lucare.Feng on 2017/3/17.
 */
public class NIORpcServiceFactory {

    public static <T> T getservice(Class<T> api) {
        return (T) Proxy.newProxyInstance(api.getClassLoader(), new Class<?>[]{api}, new NIORemotingServiceProxy());
    }

    public static void putService(final Object service, int port) throws ClassNotFoundException, NoSuchMethodException {
        try {
            Handler handler = new ServerHandler(service);
            Selector selector = Selector.open(); // 打开选择器
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("nio server started......");
            while (true) {
                int nKeys = selector.select();
                if (nKeys > 0) {
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> it = selectedKeys.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        if (key.isAcceptable()) {
                            System.out.println("Server: SelectionKey is acceptable.");
                            handler.handleAccept(key);
                        } else if (key.isReadable()) {
                            System.out.println("Server: SelectionKey is readable.");
                            handler.handleRead(key);
                        } else if (key.isWritable()) {
                            System.out.println("Server: SelectionKey is writable.");
                            handler.handleWrite(key);
                        }
                        it.remove();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
