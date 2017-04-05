package com.fcs.nio.complex;

import java.io.IOException;
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
    ByteBuffer input = ByteBuffer.allocate(128);
    ByteBuffer output = ByteBuffer.allocate(128);
    static final int READING = 0, SENDING = 1;
    int state = READING;

    public MyHandler(Selector selector,SocketChannel socketChannel) throws IOException {
        socket = socketChannel;
        socketChannel.configureBlocking(false);
        sk = socket.register(selector, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();

    }

    @Override
    public void run() {
        if (state == READING){
            read();
        }else if (state == SENDING) {
            send();
        }
    }

    void read(){

    }

    void send(){

    }

}
