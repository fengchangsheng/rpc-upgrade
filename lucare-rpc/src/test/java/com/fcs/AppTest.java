package com.fcs;

import com.fcs.bio.simple.RpcServiceFactory;
import com.fcs.demo.HelloService;
import com.fcs.demo.impl.HelloServiceImpl;
import com.fcs.netty.EchoClient;
import com.fcs.nio.simple.NIORpcServiceFactory;
import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

    @Override
    protected void setUp() throws Exception {


    }
    @Override
    protected void tearDown() throws Exception {

    }

    public void testForSimple(){
        HelloService helloService = RpcServiceFactory.getservice(HelloService.class);
        helloService.sayHello("lucare");
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
