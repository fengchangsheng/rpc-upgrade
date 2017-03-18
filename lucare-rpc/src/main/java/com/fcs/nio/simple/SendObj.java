package com.fcs.nio.simple;

import java.io.Serializable;

/**
 * Created by Lucare.Feng on 2017/3/18.
 */
public class SendObj implements Serializable {

    private String methodName;
    private Class<?>[] paramTypes;
    private Object[] agrs;

    public SendObj(String methodName, Class<?>[] paramTypes, Object[] agrs) {
        this.methodName = methodName;
        this.paramTypes = paramTypes;
        this.agrs = agrs;
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
