package com.fcs.netty.complex.handler;

import com.fcs.bio.complex.remoting.RPCHolder;
import com.fcs.netty.complex.codec.Header;
import com.fcs.netty.complex.codec.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Lucare.Feng on 2017/4/26.
 */
public class RPCClientHandler extends SimpleChannelInboundHandler<Object> {

    private RPCHolder rpcHolder;

    public RPCClientHandler(RPCHolder rpcHolder) {
        this.rpcHolder = rpcHolder;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("connect has establish");
        Header header = new Header();
        Message message = new Message();
        message.setHeader(header);
        message.setServiceName(rpcHolder.getServiceName());
        message.setMethodName(rpcHolder.getMethodName());
        message.setParamTypes(rpcHolder.getParameterTypes());
        message.setArgs(rpcHolder.getArgs());
        ctx.write(message);
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("result is " + o);
        rpcHolder.setResult(o);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
