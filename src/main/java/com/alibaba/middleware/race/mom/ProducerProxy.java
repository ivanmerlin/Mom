package com.alibaba.middleware.race.mom;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by ivanmerlin on 2015/8/5.
 */
public class ProducerProxy {
    private static String brokerIp;
    public static final String TYPE="producer";
    public static final int PORT = 9999;

    public static void setBrokerIp(String brokerIp) {
        ProducerProxy.brokerIp = brokerIp;
    }

    public static SendResult sendMessage(Message message) {
        message=preProcessMsg(message);
        SendResult result=null;
//        Socket socket = null;
//        try {
//            socket = new Socket(brokerIp,PORT);
//            System.out.println("connect to server");
//            ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
//            out.writeObject(message);
//
//            System.out.println("send over");
//            ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
//            result= (SendResult) in.readObject();
//            socket.close();
//            return result;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).remoteAddress(brokerIp,PORT)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new MomProviderHandler());
                        }
                    });

        }finally {
            group.shutdownGracefully();
        }

        result=new SendResult();
        result.setStatus(SendStatus.FAIL);
        return result;
    }

    private static Message preProcessMsg(Message message) {

        message.setProperty("type",TYPE);
        return message;
    }
}
