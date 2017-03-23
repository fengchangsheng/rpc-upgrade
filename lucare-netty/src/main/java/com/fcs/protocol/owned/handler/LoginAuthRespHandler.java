package com.fcs.protocol.owned.handler;

import com.fcs.protocol.owned.Header;
import com.fcs.protocol.owned.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Lucare.Feng on 2017/3/23.
 */
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        NettyMessage message = (NettyMessage)msg;
        if(message.getHeader() != null && message.getHeader().getType() == (byte)1){
            System.out.println("Login is OK");
            String body = (String)message.getBody();
            System.out.println("Recevied message body from client is " + body);
        }
        ctx.writeAndFlush(buildLoginResponse((byte)3));
    }

    private NettyMessage buildLoginResponse(byte result) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType((byte)2);
        message.setHeader(header);
        message.setBody(result);
        return message;
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

}
