package com.fcs.bio.simple;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Lucare.Feng on 2017/3/17.
 */
public class RpcServiceFactory {

    public static <T> T getservice(Class<T> api) {
        return (T) Proxy.newProxyInstance(api.getClassLoader(), new Class<?>[]{api}, new RemotingServiceProxy());
    }

    public static void putService(final Object service, int port) {
        try {
            final Class clazz = service.getClass();
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("server started......");
            while (true) {
                final Socket socket = serverSocket.accept();
                final ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("has client come on......");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                            String methodName = inputStream.readUTF();
                            Class<?>[] paramTypes = (Class<?>[]) inputStream.readObject();
                            Object[] args = (Object[]) inputStream.readObject();
                            Method method = clazz.getMethod(methodName, paramTypes);
                            Object result = method.invoke(service, args);
                            outputStream.writeObject(result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                            try {
                                outputStream.writeObject(e);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
