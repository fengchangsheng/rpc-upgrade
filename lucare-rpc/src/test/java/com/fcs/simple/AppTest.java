package com.fcs.simple;

import com.fcs.bio.simple.RpcServiceFactory;
import com.fcs.demo.HelloService;
import com.fcs.demo.impl.HelloServiceImpl;
import com.fcs.netty.EchoClient;
import com.fcs.nio.simple.NIORpcServiceFactory;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

    public void testForSimple(){
        HelloService helloService = RpcServiceFactory.getservice(HelloService.class);
        String strt = helloService.sayHello("lucare");
        System.out.println(strt);
    }

    public void testForNIOSimple(){
        for(int i= 0;i<10;i++) {
            HelloService helloService = NIORpcServiceFactory.getservice(HelloService.class);
            helloService.sayHello("lucare");
        }
    }

    public void testForNIOSimpleBlock(){
        HelloService helloService = NIORpcServiceFactory.getservice(HelloService.class);
        helloService.sayHello("lucare");
    }

    public void testForNetty(){
        try {
            new EchoClient("127.0.0.1", 1588).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
