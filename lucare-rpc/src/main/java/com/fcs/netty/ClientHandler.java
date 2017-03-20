package com.fcs.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Lucare.Feng on 2017/3/19.
 */
public class ClientHandler extends SimpleChannelInboundHandler<User> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("connect has establish");
        User user = new User("lucare",24);
        ctx.write(user);
        ctx.flush();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, User user) throws Exception {
        System.out.println("client received :"+ user.getName());
    }
}
