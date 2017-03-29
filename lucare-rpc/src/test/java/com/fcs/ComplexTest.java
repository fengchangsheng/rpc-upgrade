package com.fcs;

import com.fcs.bio.complex.remoting.ServiceLocator;
import com.fcs.demo.HelloService;

import java.io.IOException;

/**
 * Created by Lucare.Feng on 2017/3/29.
 */
public class ComplexTest {

    public static void main(String[] args) {
        try {
            ServiceLocator locator = new ServiceLocator(HelloService.class, "HelloService", "127.0.0.1", 12580, "test");
        } catch (IOException e) {
            e.printStackTrace();
        }  catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
