package com.fastsky.netty.client;


import com.fastsky.api.HelloService;

public class FastSkyNettyClientTest {
    public static void main(String[] args) {
        FastSkyNettyClient client = new FastSkyNettyClient();
        HelloService service = (HelloService) client.getBean(HelloService.class);
        System.out.println(service.sayHello());
    }

}