package com.fcs.simple;

import com.fcs.nio.simple.NIORpcServiceFactory;
import com.fcs.service.HelloService;
import com.fcs.service.impl.HelloServiceImpl;

/**
 * Created by Lucare.Feng on 2017/4/26.
 */
public class NIOServerTest {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        try {
            NIORpcServiceFactory.expose(helloService, 1234);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
