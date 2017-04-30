package com.fcs.netty.complex.handler;

import com.fcs.bio.complex.server.contexts.AppContext;
import com.fcs.bio.complex.server.contexts.AppContextManager;
import com.fcs.bio.complex.server.contexts.ServiceSkeleton;
import com.fcs.netty.complex.codec.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Lucare.Feng on 2017/4/26.
 */
public class RPCServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("has a client come on ..");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Message message = (Message) msg;
        Object object = process(message);
        ctx.write(object);
        ctx.flush();
    }

    private Object process(Message message) {
        AppContext appContext = AppContextManager.getAppContextImpl("test");
        ServiceSkeleton serviceSkeleton = appContext.getServiceSkeleton(message.getServiceName());
        Object service = serviceSkeleton.getService();
        final Class clazz = service.getClass();
        Object result = null;
        try {
            Method method = clazz.getMethod(message.getMethodName(), message.getParamTypes());
            result = method.invoke(service, message.getArgs());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
