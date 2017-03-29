package com.fcs.bio.complex;

import com.fcs.bio.complex.server.contexts.AppContext;
import com.fcs.bio.complex.server.contexts.AppContextManager;
import com.fcs.bio.complex.server.processor.ProcessprThread;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

/**
 * Created by Lucare.Feng on 2017/3/27.
 */
public class RpcServer {

    public void start(){
        ServerConfig sc = new ServerConfig("127.0.0.1",12345) ;
        startAppContexts(sc);
        startBIOServer(sc);
    }

    private void startAppContexts(ServerConfig conf){
        String name = "test";
        System.out.println("[KEY-STONE-CONTEXT] {context="+name+"} begin starting...") ;
        AppContext context = new AppContext(name);
        AppContextManager.register(context) ;
        System.out.println("[KEY-STONE-CONTEXT] {context="+name+"} started successfully.\n");
    }

    private void startBIOServer(ServerConfig serverConfig){
        try {
            ServerSocket serverSocket = new ServerSocket(serverConfig.getTcpPort());
            System.out.println("server started......");
            while (true) {
                final Socket socket = serverSocket.accept();
                final ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("has client come on......");
                new Thread(new ProcessprThread(outputStream, socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
