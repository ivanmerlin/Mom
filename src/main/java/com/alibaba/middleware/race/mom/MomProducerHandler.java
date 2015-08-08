package com.alibaba.middleware.race.mom;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * Created by xmc1993 on 2015/8/6.
 */
public class MomProducerHandler extends ChannelInboundHandlerAdapter {

    public MomProducerHandler(){

    }

    //TODO 需要调用SUPER的方法么？

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /**连接成功的时候发送主题信息*/



    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //TODO 删除System语句 提高性能？

        if(msg instanceof SendResult){
            /**让channel不在处于锁定的状态*/
            ctx.channel().notifyAll();

            if(((SendResult) msg).getStatus()==SendStatus.FAIL.SUCCESS){
                System.out.println("发布信息成功。");
            }else{
                System.out.println("发布消息失败:"+((SendResult) msg).getInfo());
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
