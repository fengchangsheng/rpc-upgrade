package com.fcs;

import com.fcs.demo.HelloService;
import com.fcs.nio.complex.remoting.NIOServiceFactory;
import com.fcs.util.ConcurrentUtil;

/**
 * Created by Lucare.Feng on 2017/4/7.
 * NIO complex 客户端测试（服务端直接启动-NIOServerBootrap）
 */
public class NIOComplexTest {

    public static void main(String[] args) {
//        singleTest();
        moreThreadTest();  //加上sk.cancel()或者socket.close()一开就挂了 是否需要关闭？何时关闭？需要session?
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
