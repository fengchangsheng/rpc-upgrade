package com.fcs.nio.complex.transpot;

import java.io.IOException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Lucare.Feng on 2017/4/5.
 * http://gee.cs.oswego.edu/dl/cpjslides/nio.pdf
 */
public class Reactor implements Runnable {

    private final Selector selector;

    public Reactor() throws IOException {
        selector = Selector.open();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                Iterator<SelectionKey> it = selected.iterator();
                while (it.hasNext()) {
                    dispatch(it.next());
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

    public void registerChannel(SelectableChannel channel, int ops) throws IOException {
        if (channel instanceof ServerSocketChannel) {
            ServerSocketChannel socketChannel = (ServerSocketChannel) channel;
            channel.register(selector, ops, new Acceptor(socketChannel, selector));
        }
    }
}
