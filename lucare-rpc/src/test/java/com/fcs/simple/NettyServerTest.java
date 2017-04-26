package com.fcs.simple;

import com.fcs.netty.simple.EchoServer;

/**
 * Created by Lucare.Feng on 2017/4/26.
 */
public class NettyServerTest {

    public static void main(String[] args) {
        try {
            new EchoServer(1588).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
