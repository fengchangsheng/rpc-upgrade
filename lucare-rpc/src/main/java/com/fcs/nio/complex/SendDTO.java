package com.fcs.nio.complex;

import java.io.Serializable;

/**
 * Created by Lucare.Feng on 2017/4/7.
 * 使用Json序列化时不需要实现序列化接口
 */
public class SendDTO implements Serializable{

    private String serviceName;
    private String methodName;
    private Class<?>[] paramTypes;
    private Object[] agrs;

    public SendDTO() {
    }

    public SendDTO(String serviceName, String methodName, Class<?>[] paramTypes, Object[] agrs) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.paramTypes = paramTypes;
        this.agrs = agrs;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getAgrs() {
        return agrs;
    }

    public void setAgrs(Object[] agrs) {
        this.agrs = agrs;
    }
}
