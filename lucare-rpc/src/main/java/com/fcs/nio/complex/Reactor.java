package com.fcs.nio.complex;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Lucare.Feng on 2017/4/5.
 * http://gee.cs.oswego.edu/dl/cpjslides/nio.pdf
 */
public class Reactor implements Runnable {

    private final Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public Reactor(int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        SelectionKey sk = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor());
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                selector.select();
                Set selected = selector.selectedKeys();
                Iterator it = selected.iterator();
                while (it.hasNext()) {
                    dispatch((SelectionKey)it.next());
                }
                selected.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatch(SelectionKey next) {
        Runnable r = (Runnable) next.attachment();
        if (r != null) {
            r.run();
        }
    }

    class Acceptor implements Runnable{

        @Override
        public void run() {
            try {
                SocketChannel c = serverSocketChannel.accept();
                if (c != null) {
                    new MyHandler();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
