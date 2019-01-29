package com.fastsky.bean;

import java.io.Serializable;

/**
 * ClassName: RcpResponse
 * Description: Rcp 响应bean
 *
 * @author: starryfei
 * @date: 2019-01-24 23:26
 **/
public class RcpResponse implements Serializable {
    private String requestId;
    private String name;
    private String status;
    private Object object;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RcpResponse{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", object=" + object +
                '}';
    }
}
