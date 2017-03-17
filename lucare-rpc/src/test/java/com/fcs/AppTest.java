package com.fcs;

import com.fcs.bio.simple.RpcServiceFactory;
import com.fcs.demo.HelloService;
import com.fcs.demo.impl.HelloServiceImpl;
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
        HelloService helloService = RpcServiceFactory.getservice(HelloServiceImpl.class);
        helloService.sayHello("lucare");
    }

}
