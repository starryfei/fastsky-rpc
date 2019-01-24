package com.fastsky.netty;

import java.io.Serializable;

/**
 * ClassName: RcpResponse
 * Description: TODO
 *
 * @author: starryfei
 * @date: 2019-01-24 23:26
 **/
public class RcpResponse implements Serializable {

    private String name;
    private Object object;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
