package com.fcs.netty.complex;

import com.fcs.bio.complex.ServerConfig;
import com.fcs.bio.complex.server.contexts.AppContext;
import com.fcs.bio.complex.server.contexts.AppContextManager;
import com.fcs.netty.complex.codec.Decoder;
import com.fcs.netty.complex.handler.RPCServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;


/**
 * Created by Lucare.Feng on 2017/4/26.
 */
public class NettyRpcServer {

    private static final int MAX_FRAME_LENGTH = 1024 * 1024;
    private static final int LENGTH_FIELD_LENGTH = 4;
    private static final int LENGTH_FIELD_OFFSET = 2;
    private static final int LENGTH_ADJUSTMENT = 0;
    private static final int INITIAL_BYTES_TO_STRIP = 0;

    public void start() throws InterruptedException {
        ServerConfig sc = new ServerConfig("127.0.0.1", 12345);
        startAppContexts();
        startNettyServer(sc);
    }

    private void startAppContexts() {
        String name = "test";
        System.out.println("[FCS-CONTEXT] {context=" + name + "} begin starting...");
        AppContext context = new AppContext(name);
        AppContextManager.register(context);
        System.out.println("[FCS-CONTEXT] {context=" + name + "} started successfully.\n");
    }


    private void startNettyServer(ServerConfig serverConfig) throws InterruptedException {
        int port = serverConfig.getTcpPort();
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new ObjectDecoder(1024 * 1024,
//                                    ClassResolvers.weakCachingResolver(this.getClass().getClassLoader())));
                            ch.pipeline().addLast(new Decoder(MAX_FRAME_LENGTH,LENGTH_FIELD_LENGTH,LENGTH_FIELD_OFFSET,LENGTH_ADJUSTMENT,INITIAL_BYTES_TO_STRIP));
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new RPCServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
