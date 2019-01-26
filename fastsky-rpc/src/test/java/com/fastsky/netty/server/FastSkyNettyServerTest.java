package com.fastsky.netty.server;


import com.fastsky.api.HelloServiceImpl;
import com.fastsky.bean.BeanRegister;

public class FastSkyNettyServerTest {
    public static void main(String[] args) {
        BeanRegister.getInstance().registerBeans(HelloServiceImpl.class);
        FastSkyNettyServer server = new FastSkyNettyServer();
        server.start(9122);
    }
}