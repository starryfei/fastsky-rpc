package com.fastsky.netty.client;

import com.fastsky.bean.RcpRequest;
import com.fastsky.bean.RcpResponse;
import com.fastsky.bean.BeanFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * ClassName: FastSkyNettyClient
 * Description: Netty 消费端调用服务实现
 *
 * @author: starryfei
 * @date: 2019-01-24 22:33
 **/
public class FastSkyNettyClient extends SimpleChannelInboundHandler<Object> {
    private static EventLoopGroup work;
    private RcpResponse response;
    private Object threadObj = new Object();

    private RcpResponse connect(RcpRequest dataBean) throws InterruptedException {
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
                    ch.pipeline().addLast(FastSkyNettyClient.this);
                }
            }).option(ChannelOption.SO_KEEPALIVE, true);;
            ChannelFuture future = boot.connect("localhost", 9122).sync();
            future.channel().writeAndFlush(dataBean).sync();
            synchronized (threadObj) {
                threadObj.wait(); // 未收到响应，使线程等待
            }

            System.out.println(response);
            if(response != null) {
                future.channel().closeFuture().sync();
            }
            return response;
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
             RcpResponse response = connect(dataBean);
             System.out.println(response);
             if ("400".equals(response.getStatus())) {
                 throw new RuntimeException(response.getStatus()+"rcp run error");
             } else {
                 return response.getObject();
             }
         });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        response = (RcpResponse) msg;
        synchronized (threadObj) {
            threadObj.notifyAll(); // 收到响应，唤醒线程
        }
//        beanFactory.putBean(response);
    }
}
