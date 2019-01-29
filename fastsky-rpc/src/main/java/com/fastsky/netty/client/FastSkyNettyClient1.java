package com.fastsky.netty.client;

import com.fastsky.bean.RcpRequest;
import com.fastsky.bean.RcpResponse;
import com.fastsky.netty.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * ClassName: FastSkyNettyClient1
 * Description: TODO
 *
 * @author: starryfei
 * @date: 2019-01-29 23:13
 **/
public class FastSkyNettyClient1 {
    private static EventLoopGroup work;

    private void connect() throws InterruptedException {
        work = new NioEventLoopGroup();
        try {
            Bootstrap boot = new Bootstrap();
            boot.group(work);
            boot.channel(NioSocketChannel.class);
            boot.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ObjectDecoder(1024,
                            ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                    ch.pipeline().addLast(new ObjectEncoder());
                    ch.pipeline().addLast(new ClientHandler());
                }
            }).option(ChannelOption.SO_KEEPALIVE, true);;
            ChannelFuture future = boot.connect("localhost", 9122).sync();
            future.channel().closeFuture().sync();
        } finally {
            work.shutdownGracefully();

        }

    }

    /**
     * 获取接口的代理对象
     * @param clazz
     * @return
     */
    public Object getBean(Class<?> clazz) {

        return Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz}, (proxy, method, args) -> {
            RcpRequest dataBean = new RcpRequest();
            dataBean.setId(UUID.randomUUID().toString());
            dataBean.setClassName(clazz.getName());
            dataBean.setMethodName(method.getName());
            dataBean.setArgType(method.getParameterTypes());
            dataBean.setArgs(args);
            connect();
            ClientHandler handler = new ClientHandler();
            RcpResponse response = (RcpResponse) handler.sendRequest(dataBean);
            System.out.println(response);
            if ("400".equals(response.getStatus())) {
                throw new RuntimeException(response.getStatus()+"rcp run error");
            } else {
                return response.getObject();
            }
        });

    }
}
