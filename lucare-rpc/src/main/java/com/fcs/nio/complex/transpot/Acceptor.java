package com.fcs.nio.complex.transpot;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by Lucare.Feng on 2017/4/7.
 */
public class Acceptor implements Runnable {

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public Acceptor(ServerSocketChannel serverSocketChannel,Selector selector) {
        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            SocketChannel c = serverSocketChannel.accept();
            if (c != null) {
                new MyHandler(selector, c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
