package com.fastsky.netty.handler;

import com.fastsky.bean.RcpRequest;
import com.fastsky.bean.RcpResponse;
import io.netty.channel.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * ClassName: ClientHandler
 * Description: TODO
 *
 * @author: starryfei
 * @date: 2019-01-29 22:59
 **/
public class ClientHandler extends SimpleChannelInboundHandler<Object> {
    private final Map<String, Object> dataMap = new ConcurrentHashMap<>();
    private Channel channel;
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        RcpResponse response = (RcpResponse) msg;
        dataMap.put(response.getRequestId(),response);
        System.out.println(response);

    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    public Object sendRequest(RcpRequest request) {
        final CountDownLatch latch = new CountDownLatch(1);
        final Object[] result = new Object[1];
        channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                latch.countDown();

            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        result[0] = dataMap.get(request.getId());
        return result[0];
    }
}
