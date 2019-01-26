package com.fastsky.netty.server;

import com.fastsky.netty.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * ClassName: FastSkyNettyServer
 * Description: Netty 服务提供端实现
 *
 * @author: starryfei
 * @date: 2019-01-24 18:50
 **/
public class FastSkyNettyServer {

    private static EventLoopGroup work = new NioEventLoopGroup();
    private static EventLoopGroup boos = new NioEventLoopGroup();
    private static Channel channel;

    /**
     * 启动server
     */
    public void start(int port){
        final ServerBootstrap server = new ServerBootstrap();
        server.group(work,boos).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //添加对象解码器 负责对序列化POJO对象进行解码 设置对象序列化最大长度为1M 防止内存溢出
                        //设置线程安全的WeakReferenceMap对类加载器进行缓存 支持多线程并发访问  防止内存溢出
                        socketChannel.pipeline().addLast(new ObjectDecoder(1024*1024,
                                ClassResolvers.weakCachingResolver(this.getClass().getClassLoader())));

                        //添加对象编码器 在服务器对外发送消息的时候自动将实现序列化的POJO对象编码
                        socketChannel.pipeline().addLast(new ObjectEncoder());
                        socketChannel.pipeline().addLast(new ServerHandler());

                    }
                });

        ChannelFuture future = null;
        try {
            future = server.bind(port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (future.isSuccess()) {
            System.out.println("connect success");
        }
        channel = future.channel();

    }


    /**
     * 停止server
     */
    public void stop() {
        Runtime.getRuntime().addShutdownHook(new ShutServer());
        try {
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * shutdown钩子事件
     */
    static class ShutServer extends Thread{
        @Override
        public void run() {
            work.shutdownGracefully();
            boos.shutdownGracefully();

            System.out.println("server stoped");
        }
    }
}
