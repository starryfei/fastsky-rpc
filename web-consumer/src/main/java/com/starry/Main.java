package com.starry;

import com.fastsky.socket.client.FastSkyClient;
import com.starry.service.HelloService;

/**
 * ClassName: Main
 * Description: 获取服务
 *
 * @author: starryfei
 * @date: 2019-01-22 17:22
 **/
public class Main {
    public static void main(String[] args) {
        FastSkyClient client = new FastSkyClient();
        client.start();
        HelloService helloService = (HelloService) client.getBean(HelloService.class);
        String hellp = helloService.sayHello();
        System.out.println(hellp);
        client.destroy();
    }
}
