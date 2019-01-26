package com.fastsky.netty.client;

import com.fastsky.netty.handler.NettyClienthandler;
import com.fastsky.netty.bean.BeanFactory;
import com.fastsky.netty.bean.DataBean;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ClassName: FastSkyNettyClient
 * Description: TODO
 *
 * @author: starryfei
 * @date: 2019-01-24 22:33
 **/
public class FastSkyNettyClient {
    private static EventLoopGroup work;
    private BeanFactory beanFactory = BeanFactory.getInstance();
    public void connect(DataBean dataBean) {
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
                    ch.pipeline().addLast(new NettyClienthandler(dataBean));
                }
            });
            ChannelFuture future = boot.connect("localhost", 9122).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取接口的代理对象
     * @param clazz
     * @return
     */
    public Object getBean(Class<?> clazz) {

         Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                DataBean dataBean = new DataBean();
                dataBean.setClassName(clazz.getName());
                dataBean.setMethodName(method.getName());
                dataBean.setArgType(method.getParameterTypes());
                dataBean.setArgs(args);
                connect(dataBean);
                return beanFactory.getBean(clazz);
            }
        });
         return null;

    }

    public void destroy(){
        work.shutdownGracefully();
    }
}
