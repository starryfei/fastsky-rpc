package com.fastsky.handler;

import com.fastsky.netty.DataBean;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * ClassName: NettyHandler
 * Description: TODO
 *
 * @author: starryfei
 * @date: 2019-01-24 18:57
 **/
public class NettyHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object obj) throws Exception {
        DataBean dataBean = (DataBean) obj;
        String className = dataBean.getClassName();
        String methodName = dataBean.getMethodName();
        Class[] parTypes = dataBean.getArgType();
        Object[] args = dataBean.getArgs();

        Class<?> clazz = null;
        Object object = clazz.getMethod(methodName,parTypes).invoke(clazz.newInstance(),args);
        channelHandlerContext.writeAndFlush(object);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
