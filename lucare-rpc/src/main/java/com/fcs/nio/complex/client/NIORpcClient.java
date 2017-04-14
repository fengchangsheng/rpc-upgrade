package com.fcs.nio.complex.client;

import com.alibaba.fastjson.JSON;
import com.fcs.bio.complex.remoting.RPCHolder;
import com.fcs.nio.complex.codec.CodecHelper;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by Lucare.Feng on 2017/3/29.
 */
public class NIORpcClient {

    private String host;

    private int port;

    private SocketChannel socketChannel;

    public NIORpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void invoke(Object request) throws IOException, ClassNotFoundException {
        if (request instanceof RPCHolder) {
            RPCHolder rpcHolder = (RPCHolder) request;
            connect();
            write(rpcHolder);
            read(rpcHolder);
        }
    }

    public void connect() throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
        socketChannel = SocketChannel.open(inetSocketAddress);
        System.out.println("who is "+socketChannel.isConnected()+"   "+Thread.currentThread().getName());
        socketChannel.configureBlocking(false);
        System.out.println("has come on...."+Thread.currentThread().getName());
    }

    public void write(RPCHolder rpcHolder) throws IOException {
        ByteBuffer buffer = CodecHelper.encodeRequest(rpcHolder);
        buffer.flip();
        socketChannel.write(buffer);
    }

    public void read(RPCHolder rpcHolder) throws IOException {
        System.out.println("read......"+ Thread.currentThread().getName());
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        Object result;
        while (true) {
            byteBuffer.clear();
            int readBytes = socketChannel.read(byteBuffer);
            if (readBytes > 0) {
                byte[] in = byteBuffer.array();
                result = JSON.parseObject(in, Object.class);
                System.out.println("Client: data = " + result+ "  "+Thread.currentThread().getName());
                socketChannel.close();
                break;
            }
        }
        if (result != null) {
            rpcHolder.setResult(result);
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
