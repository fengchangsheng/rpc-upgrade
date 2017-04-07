package com.fcs.nio.complex.remoting;

import com.fcs.bio.complex.remoting.RemotingTicket;
import com.fcs.nio.complex.client.NIORpcClient;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Lucare.Feng on 2017/3/17.
 */
public class NIORemoteServiceProxy implements InvocationHandler {

    private NIOServiceLocator<?> locator;

    private NIORpcClient client = null;

    public NIORemoteServiceProxy(NIOServiceLocator<?> locator) throws IOException, InterruptedException {
        this.locator = locator;
        initClient();
    }

    private void initClient() throws IOException, InterruptedException {
        client = new NIORpcClient(locator.getHost(), locator.getPort());
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
//        ticket.setTicket(getTicket());
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
