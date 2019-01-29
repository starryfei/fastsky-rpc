package com.fastsky.netty.client;


import com.fastsky.api.HelloService;

public class FastSkyNettyClient1Test {
    public static void main(String[] args) {
        FastSkyNettyClient1 client = new FastSkyNettyClient1();
        HelloService service = (HelloService) client.getBean(HelloService.class);
        System.out.println(service.sayHello());
    }

}