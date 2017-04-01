package com.fcs.bio.complex.remoting;

import com.fcs.bio.complex.client.RpcClient;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Lucare.Feng on 2017/3/17.
 */
public class RemotingServiceProxy implements InvocationHandler {

    private ServiceLocator<?> locator ;

    private RpcClient client = null ;

    public RemotingServiceProxy(ServiceLocator<?> locator) throws IOException, InterruptedException {
        this.locator = locator ;
        initClient() ;
    }

    private void initClient() throws IOException, InterruptedException {
        client = new RpcClient(locator.getHost(), locator.getPort()) ;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return handleRemoteMethod(method, args);
    }

    protected Object handleRemoteMethod(Method method, Object[] args) throws Throwable {
        return invokeWithSync(method, args) ;
    }

    protected Object invokeWithSync(Method method, Object args[]) throws Throwable {
        RemotingTicket ticket = new RemotingTicket() ;
//        ticket.setTicket(getTicket()) ;
        ticket.setAppName(locator.getAppName()) ;
        ticket.setServiceName(locator.getServiceName()) ;
        ticket.setMethodName(method.getName()) ;
        ticket.setParameterTypes(method.getParameterTypes());
        ticket.setArgs(args) ;
        ticket.setReturnType(method.getReturnType());
        client.invoke(ticket);
        return ticket.getResult();
    }

}
