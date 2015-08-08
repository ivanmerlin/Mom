package com.alibaba.middleware.race.mom;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by xmc1993 on 2015/8/6.
 */
public class MomConsumerHandler extends SimpleChannelInboundHandler<Object> {
    Message message;
    public MomConsumerHandler(Message message){
        this.message=message;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        /**当通道被激活的时候发送Message*/
        ctx.write(message);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        /**可能需要用OutboundHandler么？*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
