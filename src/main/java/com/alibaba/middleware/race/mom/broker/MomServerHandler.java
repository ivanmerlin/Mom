package com.alibaba.middleware.race.mom.broker;

import com.alibaba.middleware.race.mom.Message;
import com.alibaba.middleware.race.mom.SendResult;
import com.alibaba.middleware.race.mom.SendStatus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by xmc1993 on 2015/8/6.
 */
public class MomServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("Get a new Message!");



        /**如果接收到的是一个Message*/
        if(msg instanceof Message){
            System.out.println("编码成功..");


            //TODO 当收到一条信息以后开启一条新的线程去处理？
        }

        SendResult sendResult = new SendResult();
        sendResult.setMsgId("987");
        sendResult.setInfo("Netty success!");
        sendResult.setStatus(SendStatus.SUCCESS);

        ctx.channel().writeAndFlush(sendResult).sync();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("接收到了一个新的connection！");
    }
}
