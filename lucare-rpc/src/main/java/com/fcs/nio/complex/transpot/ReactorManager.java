package com.fcs.nio.complex.transpot;

import java.io.IOException;
import java.nio.channels.SelectableChannel;

/**
 * Created by Lucare.Feng on 2017/4/7.
 */
public class ReactorManager {

    private boolean started;

    private Reactor reactor;

    public synchronized void start() throws IOException {
        if (!started) {
            reactor = new Reactor();
            reactor.start();
            started = true;
        }
    }

    public void registerChannel(SelectableChannel channel, int ops) throws IOException {
        reactor.registerChannel(channel, ops) ;
    }

}
