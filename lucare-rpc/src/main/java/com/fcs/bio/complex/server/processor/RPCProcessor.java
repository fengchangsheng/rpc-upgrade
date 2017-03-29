package com.fcs.bio.complex.server.processor;

import com.fcs.bio.complex.server.contexts.AppContext;
import com.fcs.bio.complex.server.contexts.AppContextManager;
import com.fcs.bio.complex.server.contexts.ServiceSkeleton;
import com.fcs.bio.complex.support.CommonUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by Lucare.Feng on 2017/3/29.
 */
public class RPCProcessor {

    private static ThreadLocal<AppContext> contextThreadLocal = new ThreadLocal<>() ;

    public static Object proccess(String contextName, String serviceName, String methodName, Object... args) throws Exception {
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        Object result = null;
        try {
            AppContext appContext = AppContextManager.getAppContextImpl(contextName);
            ServiceSkeleton skeleton = appContext.getServiceSkeleton(serviceName);
//            MethodHolder methodHolder = skeleton.getMethodHolder(methodName);
//            Method method = skeleton.getMethod(methodName);

            contextThreadLocal.set(appContext);
            //TODO: build distribute context?
//            Thread.currentThread().setContextClassLoader(appContext.getAppClassLoader());

//            result = method.invoke(skeleton.getService(), args);


        } catch (Exception e) {
            System.err.println("contextName=" + contextName + ", serviceName=" + serviceName + ", methodName=" + methodName + ", args=" + Arrays.toString(args) + " with ex:" + CommonUtils.formatThrowable(e));
            throw e;
        } finally {
            Thread.currentThread().setContextClassLoader(currentClassLoader);
            contextThreadLocal.remove();
        }
        return result;
    }

}
