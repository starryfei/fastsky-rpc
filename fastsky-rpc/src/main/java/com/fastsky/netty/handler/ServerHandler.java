package com.fastsky.netty.handler;

import com.fastsky.bean.BeanRegister;
import com.fastsky.bean.RcpResponse;
import com.fastsky.bean.RcpRequest;
import io.netty.channel.*;

/**
 * ClassName: ServerHandler
 * Description: 消息的接受和响应
 *
 * @author: starryfei
 * @date: 2019-01-24 18:57
 *
 **/
@ChannelHandler.Sharable
public class ServerHandler extends ChannelDuplexHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("recive: " + msg);
        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        RcpRequest request = (RcpRequest) msg;

        String className = request.getClassName();
        String methodName = request.getMethodName();
        Class[] parTypes = request.getArgType();
        Object[] args = request.getArgs();

        Class<?> clazz = BeanRegister.getInstance().getBean(className);
        // 将数据回传给客户端
        RcpResponse response = new RcpResponse();
        response.setRequestId(request.getId());
        response.setName(className);
        try {
            Object object = clazz.getMethod(methodName,parTypes).invoke(clazz.newInstance(),args);
            response.setStatus("200");
            response.setObject(object);
        } catch (Exception e) {
            response.setStatus("400");
        }
        System.out.println(response);
        // 写消息并将通知关闭
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);

    }
}
