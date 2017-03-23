package com.fcs.protocol.owned.handler;

import com.fcs.protocol.owned.Header;
import com.fcs.protocol.owned.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Lucare.Feng on 2017/3/23.
 */
public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("LoginAuthReqHandler...." );
        ctx.writeAndFlush(buildLoginReq());
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage)msg;
        if(message.getHeader() != null && message.getHeader().getType() == (byte)2){
            System.out.println("Received from server response");
        }
        ctx.fireChannelRead(msg);
    }

    private NettyMessage buildLoginReq() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType((byte)1);
        message.setHeader(header);
        message.setBody("It is request");
        return message;
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

}
