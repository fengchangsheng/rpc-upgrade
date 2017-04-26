package com.fcs.service.impl;

import com.fcs.service.HelloService;

/**
 * Created by Lucare.Feng on 2017/3/17.
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
//        System.out.println(1/0);//试试异常如何处理
        System.out.println("hello," + name);
        return "hi,love u";
    }
}
