package com.fcs.nio.complex;


import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Lucare.Feng on 2017/4/7.
 */
public class NIOServerBootrap {

    private static final AtomicBoolean started = new AtomicBoolean() ;

    public static void main(String[] args) {
        if (started.compareAndSet(false, true)) {
            try {
                new NIORpcServer().start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
