package com.fcs.demo.impl;

import com.fcs.demo.HelloService;

/**
 * Created by Lucare.Feng on 2017/3/17.
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public void sayHello(String name) {
//        System.out.println(1/0);//试试异常如何处理
        System.out.println("hello," + name);
    }
}
