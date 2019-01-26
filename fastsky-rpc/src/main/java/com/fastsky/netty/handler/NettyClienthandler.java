package com.fastsky.netty.handler;

import com.fastsky.netty.bean.BeanFactory;
import com.fastsky.netty.bean.DataBean;
import com.fastsky.netty.bean.RcpResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * ClassName: NettyClienthandler
 * Description: TODO
 *
 * @author: starryfei
 * @date: 2019-01-24 22:44
 **/
public class NettyClienthandler extends SimpleChannelInboundHandler<Object> {
    private DataBean dataBean;
    private BeanFactory beanFactory = BeanFactory.getInstance();
    public NettyClienthandler(DataBean dataBean) {
        this.dataBean = dataBean;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        channelHandlerContext.writeAndFlush(dataBean);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RcpResponse response = (RcpResponse) msg;
        beanFactory.putBean(response);
    }
}
