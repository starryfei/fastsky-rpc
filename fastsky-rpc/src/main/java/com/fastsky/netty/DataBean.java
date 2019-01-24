package com.fastsky.netty;

import java.io.Serializable;

/**
 * ClassName: DataBean
 * Description: TODO
 *
 * @author: starryfei
 * @date: 2019-01-24 19:13
 **/
public class DataBean implements Serializable {
    private static final long serialVersionUID = 4228051882802183587L;

    private String className;
    private String methodName;
    private Class<?>[] argType;
    private Object[] args;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getArgType() {
        return argType;
    }

    public void setArgType(Class<?>[] argType) {
        this.argType = argType;
    }

    public void setArgs(Object[] args) {
        this.args = args;

    }
}
