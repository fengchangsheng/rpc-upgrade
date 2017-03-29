package com.fcs.bio.complex.client;

import com.fcs.bio.complex.remoting.RPCHolder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Lucare.Feng on 2017/3/29.
 */
public class RpcClient {

    private String host;

    private int port;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void invoke(Object request) throws IOException, ClassNotFoundException {
        if (request instanceof RPCHolder) {
            RPCHolder rpcHolder = (RPCHolder) request;
            Socket socket = new Socket(host, port);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeUTF(rpcHolder.getAppName());
            outputStream.writeUTF(rpcHolder.getMethodName());
            outputStream.writeObject(rpcHolder.getReturnType());
            outputStream.writeObject(rpcHolder.getArgs());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Object result = inputStream.readObject();
            System.out.println(result);
//            if (result instanceof Throwable)
//                throw (Throwable) result;
            if (result != null) {
                rpcHolder.setResult(result);
            }
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
