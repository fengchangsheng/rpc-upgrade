package com.fcs.netty.complex.client;

import com.fcs.bio.complex.remoting.RPCHolder;
import com.fcs.netty.complex.codec.Encoder;
import com.fcs.netty.complex.handler.RPCClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import java.net.InetSocketAddress;

/**
 * Created by Lucare.Feng on 2017/4/26.
 */
public class NettyRpcClient {

    private String host;

    private int port;

    public NettyRpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void invoke(Object request) throws InterruptedException, ClassNotFoundException {
        if (request instanceof RPCHolder) {
            RPCHolder rpcHolder = (RPCHolder) request;
            connect(rpcHolder);
            System.out.println("hah "+rpcHolder.getResult());
        }
    }

    public void connect(final RPCHolder rpcHolder) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap(); // (2)
            b.group(group)
                    .channel(NioSocketChannel.class) // (3)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<io.netty.channel.socket.SocketChannel>() {
                        @Override
                        protected void initChannel(io.netty.channel.socket.SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ObjectDecoder(1024 ,
                                    ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                            socketChannel.pipeline().addLast(new Encoder());
                            socketChannel.pipeline().addLast(new RPCClientHandler(rpcHolder));
                        }
                    });
            ChannelFuture future = b.connect().sync();
            future.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully().sync();
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
