package com.fcs;

import com.fcs.bio.simple.RpcServiceFactory;
import com.fcs.demo.HelloService;
import com.fcs.demo.impl.HelloServiceImpl;
import com.fcs.nio.simple.NIORpcServiceFactory;

/**
 * Created by Lucare.Feng on 2017/3/18.
 */
public class ProductServiceTest {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServiceFactory.expose(helloService,1234);
        try {
            NIORpcServiceFactory.expose(helloService, 1234);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
