package com.fcs.bio.complex;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Lucare.Feng on 2017/3/27.
 */
public class ServerBootrap {

    private static final AtomicBoolean started = new AtomicBoolean() ;

    public static void main(String[] args) {
        if (started.compareAndSet(false, true)) {
            new RpcServer().start();
        }

    }
}
