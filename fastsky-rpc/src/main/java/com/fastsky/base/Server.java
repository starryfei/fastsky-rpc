package com.fastsky.base;

/**
 * ClassName: Server
 * Description: TODO
 *
 * @author: starryfei
 * @date: 2019-01-27 01:03
 **/
public interface Server {
    void connect();

    void disConnect();

    Object getBean(Class<?> cla);
}
