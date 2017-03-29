package com.fcs.bio.complex.server.processor;

import com.fcs.bio.complex.server.contexts.AppContext;
import com.fcs.bio.complex.server.contexts.AppContextManager;
import com.fcs.bio.complex.server.contexts.ServiceSkeleton;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * Created by Lucare.Feng on 2017/3/29.
 */
public class ProcessprThread implements Runnable {

    private ObjectOutputStream outputStream;
    private Socket socket;

    public ProcessprThread(ObjectOutputStream objectOutputStream, Socket socket) {
        this.outputStream = objectOutputStream;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            AppContext appContext = AppContextManager.getAppContextImpl("test");
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            String serviceName = inputStream.readUTF();
            ServiceSkeleton serviceSkeleton = appContext.getServiceSkeleton(serviceName);
            String methodName = inputStream.readUTF();
            Class<?>[] paramTypes = (Class<?>[]) inputStream.readObject();
            Object[] args = (Object[]) inputStream.readObject();
            Method method = serviceSkeleton.getClass().getMethod(methodName, paramTypes);
            Object result = method.invoke(serviceSkeleton.getService(), args);
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
}
