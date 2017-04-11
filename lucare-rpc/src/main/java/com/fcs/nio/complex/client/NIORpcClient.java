package com.fcs.nio.complex.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fcs.bio.complex.remoting.RPCHolder;
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

    public NIORpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void invoke(Object request) throws IOException, ClassNotFoundException {
        if (request instanceof RPCHolder) {
            RPCHolder rpcHolder = (RPCHolder) request;
            InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",12345);
            SocketChannel socketChannel = SocketChannel.open(inetSocketAddress);
            socketChannel.configureBlocking(false);
            ByteBuffer byteBuffer = ByteBuffer.allocate(512);
            String serviceName = rpcHolder.getServiceName();
            String methodName = rpcHolder.getMethodName();
            Class<?>[] paramType = rpcHolder.getParameterTypes();
            Object[] args = rpcHolder.getArgs();
            Object[] parameters = new Object[4] ;//使用对象数组传输的字节数是Java序列化的1/6  是json序列化的1/3
            parameters[0] = serviceName;
            parameters[1] = methodName;
            parameters[2] = paramType;
            parameters[3] = args;
            byte[] data = JSON.toJSONBytes(parameters,SerializerFeature.WriteClassName, SerializerFeature.WriteDateUseDateFormat);
//            byteBuffer.put(data);
//            byteBuffer.flip();
//            socketChannel.write(byteBuffer);
            socketChannel.write(ByteBuffer.wrap(data));
            Object result;
            while (true) {
                byteBuffer.clear();
                int readBytes = socketChannel.read(byteBuffer);
                if (readBytes > 0) {
                    byte[] in = byteBuffer.array();
                    result = JSON.parseObject(in, Object.class);
                    System.out.println("Client: data = " + result);
                    socketChannel.close();
                    break;
                }
            }
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
