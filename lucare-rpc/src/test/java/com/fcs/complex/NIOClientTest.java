package com.fcs.complex;

import com.fcs.service.HelloService;
import com.fcs.nio.complex.remoting.NIOServiceFactory;
import com.fcs.util.ConcurrentUtil;

/**
 * Created by Lucare.Feng on 2017/4/7.
 * NIO complex 客户端测试（服务端直接启动-NIOServerBootrap）
 */
public class NIOClientTest {

    public static void main(String[] args) {
        singleTest();
//        moreThreadTest();  //需要session?
//        concurrentTest();
    }

    public static void singleTest(){
        HelloService helloService = NIOServiceFactory.getService(HelloService.class);
        String res = helloService.sayHello("lucare");
        System.out.println(res);
    }

    public static void moreThreadTest(){
        for (int i = 0;i<5;i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HelloService helloService = NIOServiceFactory.getService(HelloService.class);
                    String res = helloService.sayHello("lucare");
                    System.out.println(res);
                }
            }).start();
        }
    }

    public static void concurrentTest(){
        try {
            ConcurrentUtil.timeTasks(5, new Runnable() {
                @Override
                public void run() {
                    HelloService helloService = NIOServiceFactory.getService(HelloService.class);
                    String res = helloService.sayHello("lucare");
                    System.out.println(res);
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
