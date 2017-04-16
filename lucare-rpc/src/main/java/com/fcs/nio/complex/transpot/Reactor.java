package com.fcs.nio.complex.transpot;

import java.io.IOException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Lucare.Feng on 2017/4/5.
 * http://gee.cs.oswego.edu/dl/cpjslides/nio.pdf
 */
public class Reactor extends Thread {

    private final Selector selector;
    private LinkedBlockingQueue<Object[]> register = new LinkedBlockingQueue<>() ;//channel、ops、attach
    private final AtomicBoolean wakeup = new AtomicBoolean() ;

    public Reactor() throws IOException {
        selector = Selector.open();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                wakeup.set(false);
                processRegister();
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

    private void processRegister() {
        Object[] object;
        while ((object = this.register.poll()) != null) {
            try {
                SelectableChannel channel = (SelectableChannel) object[0];
                if (!channel.isOpen())
                    continue;
                int ops = ((Integer) object[1]).intValue();
                Object attachment = object[2];
                channel.register(this.selector, ops, attachment);
            } catch (Exception e) {
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
        ServerSocketChannel serverChannel = null;
        if (channel instanceof ServerSocketChannel) {
            serverChannel = (ServerSocketChannel) channel;
        }
        Object attachment = new Acceptor(serverChannel, selector);
        //必须在当前线程里注册  否则会出现注册失败的现象
        if (this == Thread.currentThread()) {
            serverChannel.register(selector, ops, attachment);
        } else {
            this.register.offer(new Object[]{ channel, ops, attachment });
            if (wakeup.compareAndSet(false, true)) {
                this.selector.wakeup();
            }
        }
    }
}
