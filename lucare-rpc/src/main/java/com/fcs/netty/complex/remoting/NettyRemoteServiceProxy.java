package com.fcs.netty.complex.remoting;

import com.fcs.bio.complex.remoting.RemotingTicket;
import com.fcs.netty.complex.client.NettyRpcClient;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Lucare.Feng on 2017/3/17.
 */
public class NettyRemoteServiceProxy implements InvocationHandler {

    private NettyServiceLocator<?> locator;

    private NettyRpcClient client = null;

    public NettyRemoteServiceProxy(NettyServiceLocator<?> locator) throws IOException, InterruptedException {
        this.locator = locator;
        initClient();
    }

    private void initClient() throws IOException, InterruptedException {
        client = new NettyRpcClient(locator.getHost(), locator.getPort());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return handleRemoteMethod(method, args);
    }

    protected Object handleRemoteMethod(Method method, Object[] args) throws Throwable {
        return invokeWithSync(method, args);
    }

    protected Object invokeWithSync(Method method, Object args[]) throws Throwable {
        RemotingTicket ticket = new RemotingTicket();
        ticket.setAppName(locator.getAppName());
        ticket.setServiceName(locator.getServiceName());
        ticket.setMethodName(method.getName());
        ticket.setParameterTypes(method.getParameterTypes());
        ticket.setArgs(args);
        ticket.setReturnType(method.getReturnType());
        client.invoke(ticket);
        return ticket.getResult();
    }

}
