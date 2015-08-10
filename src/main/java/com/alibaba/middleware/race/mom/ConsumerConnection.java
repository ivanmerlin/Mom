package com.alibaba.middleware.race.mom;


import com.alibaba.middleware.race.mom.encode.KryoDecoder;
import com.alibaba.middleware.race.mom.encode.KryoEncoder;
import com.alibaba.middleware.race.mom.encode.KryoPool;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.IOException;


public class ConsumerConnection extends ChannelInboundHandlerAdapter{
    public static final int PORT = 9999;
    public static final int LISTEN_PORT = 9999;
    private String brokerIp;
    public static final String TYPE="consumer";
    static KryoPool pool=new KryoPool();
    private volatile Throwable exception;

    private Channel channel;

    private boolean connected;

    public static Message preProcessMsg(Message message){
        message.setProperty("type",TYPE);
        return message;
    }
    public  void setBrokerIp(String brokerIp) {
        this.brokerIp = brokerIp;
    }

    /**开始连接*/
    public void connect() throws Throwable{
        /**加入TYPE:producer属性*/
        SendResult result=null;
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).remoteAddress("127.0.0.1",PORT)
                    .option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    /**增加编码解码器*/
                    socketChannel.pipeline().addLast(new KryoDecoder(pool));
                    socketChannel.pipeline().addLast(new KryoEncoder(pool));
                    /**自己本身就是一个Handler*/
                    socketChannel.pipeline().addLast(ConsumerConnection.this);
                }
            });

            ChannelFuture channelFuture = bootstrap.connect().sync();

            /**不明白加这一步是什么意思 不知道该加不加*/
//            f.channel().closeFuture().sync();
            /**如果连接失败了*/
            if (!channelFuture.awaitUninterruptibly().isSuccess()){
                throw new Exception("connect fail");
            }

            channel = channelFuture.channel();
            connected = true;

            System.out.println("Netty Client 连接成功！");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(Message message){
        message = preProcessMsg(message);
        /**如果处于非连接状态*/
        if(!connected){
            throw new IllegalStateException("not connected");
        }

        /**发送Message*/
        ChannelFuture channelFuture = channel.writeAndFlush(message);
        channelFuture.addListener(new GenericFutureListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                System.out.println("发送订阅消息成功!");
            }
        });

    }

    /**关闭连接*/
    public void close(){

        connected=false;
        /**
         * 释放资源
         * 关闭channel
         */
        if (null != channel) {
            channel.close().awaitUninterruptibly();
            //TODO 还有其它资源需要释放吗？
            this.exception = new IOException("connection closed");
            synchronized (channel) {
                channel.notifyAll();
            }
            channel = null;
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /**连接成功的时候发送主题信息*/

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

//        if(msg instanceof SendResult){
//            /**解除channel的死锁状态*/
//            synchronized (this.channel) {
//                this.channel.notifyAll();
//            }
//            /**得到返回的结果*/
//            sendResult=(SendResult)msg;
//        }
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
