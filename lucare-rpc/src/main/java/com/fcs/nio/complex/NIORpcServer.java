package com.fcs.nio.complex;

import com.fcs.bio.complex.ServerConfig;
import com.fcs.bio.complex.server.contexts.AppContext;
import com.fcs.bio.complex.server.contexts.AppContextManager;
import com.fcs.nio.complex.transpot.ReactorManager;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

/**
 * Created by Lucare.Feng on 2017/3/27.
 */
public class NIORpcServer {

    private ReactorManager reactorManager;

    public void start() throws IOException {
        ServerConfig sc = new ServerConfig("127.0.0.1", 12345);
        startAppContexts();
        startReactorManager();
        startNIOServer(sc);
    }

    private void startAppContexts() {
        String name = "test";
        System.out.println("[FCS-CONTEXT] {context=" + name + "} begin starting...");
        AppContext context = new AppContext(name);
        AppContextManager.register(context);
        System.out.println("[FCS-CONTEXT] {context=" + name + "} started successfully.\n");
    }

    private void startReactorManager() throws IOException {
        reactorManager = new ReactorManager();
        reactorManager.start();
    }

    private void startNIOServer(ServerConfig serverConfig) throws IOException {
        String host = serverConfig.getHost();
        int port = serverConfig.getTcpPort();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.socket().bind(new InetSocketAddress(host, port));
        serverChannel.configureBlocking(false);
        reactorManager.registerChannel(serverChannel, SelectionKey.OP_ACCEPT);
    }
}
