package com.alibaba.middleware.race.mom.broker;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.alibaba.middleware.race.mom.Message;
import com.alibaba.middleware.race.mom.SendResult;
import com.alibaba.middleware.race.mom.SendStatus;
import com.alibaba.middleware.race.mom.data.DataHelper;

/**
 */
public class MomServerHandler extends ChannelInboundHandlerAdapter {
    

    DataHelper helper;
    ConfigServer configServer;
    ChannelHandlerContext myCtx;

    public MomServerHandler(ConfigServer configserver2){
       helper=new DataHelper();
       configServer=configserver2;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /**如果接收到的是一个Message*/
        if(msg instanceof Message){
            Message message=(Message)msg;
            String s=message.getProperty("type");
            if("consumer".equals(s)){
                    //收到订阅消息
                    if("consumerSubscribe".equals(message.getProperty("function"))){
//                        System.out.println("Get a new Message from consumer!"+ctx);
                        configServer.setRegist(message, this);
                    }
            }else if("producer".equals(s)){
                    //producer发来的消息只有一种
                    //TODO 消息落盘
                    helper.saveMessage(message);
//                    System.out.println("Get a new Message from producer!"+ctx);
                    ctx.writeAndFlush(configServer.sendMsg(message));
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
//        ctx.close();
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        myCtx=ctx;
    }
    
    /**
     * 需要超时重连嘛？
     */
    public void sendMsgToConsumer(Message msg){
        myCtx.channel().writeAndFlush(msg);
    }
}
