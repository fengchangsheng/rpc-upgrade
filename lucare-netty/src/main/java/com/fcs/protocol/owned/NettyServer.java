package com.fcs.protocol.owned;

import com.fcs.protocol.owned.coder.NettyMessageDecoder;
import com.fcs.protocol.owned.coder.NettyMessageEncoder;
import com.fcs.protocol.owned.handler.LoginAuthRespHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by Lucare.Feng on 2017/3/23.
 */
public class NettyServer {

    public void bind(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());

            ChannelFuture f = b.bind(port).sync();
            System.out.println("Netty time Server started at port " + port);
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4, -8, 0))
                    .addLast(new NettyMessageEncoder())
                    .addLast(new LoginAuthRespHandler());
        }

    }

    public static void main(String[] args){
        try {
            new NettyServer().bind(9080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
