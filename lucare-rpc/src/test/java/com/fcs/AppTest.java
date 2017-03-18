package com.fcs;

import com.fcs.bio.simple.RpcServiceFactory;
import com.fcs.demo.HelloService;
import com.fcs.demo.impl.HelloServiceImpl;
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
        HelloService helloService = NIORpcServiceFactory.getservice(HelloService.class);
        helloService.sayHello("lucare");
    }


}
