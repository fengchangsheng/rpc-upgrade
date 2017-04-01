package com.fcs;

import com.fcs.bio.complex.remoting.ServiceLocator;
import com.fcs.bio.complex.server.contexts.ServiceSkeleton;
import com.fcs.demo.HelloService;


/**
 * Created by Lucare.Feng on 2017/3/29.
 */
public class ComplexTest {

    public static void main(String[] args) {
        try {
            ServiceLocator locator = new ServiceLocator(HelloService.class, "HelloService", "127.0.0.1", 12345, "test");
            HelloService helloService = (HelloService) locator.getProxy();
            String res = helloService.sayHello("lucare");
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
