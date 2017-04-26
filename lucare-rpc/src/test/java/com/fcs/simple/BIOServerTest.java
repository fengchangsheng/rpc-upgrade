package com.fcs.simple;

import com.fcs.bio.simple.RpcServiceFactory;
import com.fcs.service.HelloService;
import com.fcs.service.impl.HelloServiceImpl;

/**
 * Created by Lucare.Feng on 2017/3/18.
 * 暴露接口来启动simple 服务端
 */
public class BIOServerTest {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServiceFactory.expose(helloService,1234);
    }
}
