package com.fcs;

import com.fcs.demo.HelloService;
import com.fcs.nio.complex.remoting.NIOServiceFactory;

/**
 * Created by Lucare.Feng on 2017/4/7.
 */
public class NIOComplexTest {

    public static void main(String[] args) {
        HelloService helloService = NIOServiceFactory.getService(HelloService.class);
        String res = helloService.sayHello("lucare");
        System.out.println(res);
    }

}
