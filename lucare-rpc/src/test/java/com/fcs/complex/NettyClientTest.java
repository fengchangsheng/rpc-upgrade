package com.fcs.complex;

import com.fcs.netty.complex.remoting.NettyServiceFactory;
import com.fcs.service.HelloService;

/**
 * Created by Lucare.Feng on 2017/4/26.
 */
public class NettyClientTest {

    public static void main(String[] args) {
        HelloService helloService = NettyServiceFactory.getService(HelloService.class);
        String res = helloService.sayHello("lucare");
        System.out.println(res);
    }
}
