package com.fcs.netty.complex;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Lucare.Feng on 2017/4/26.
 */
public class NettyServerBootrap {

    private static final AtomicBoolean started = new AtomicBoolean() ;

    public static void main(String[] args) {
        if (started.compareAndSet(false, true)) {
            try {
                new NettyRpcServer().start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
