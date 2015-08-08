package com.alibaba.middleware.race.mom;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import com.alibaba.middleware.race.mom.encode.KryoDecoder;
import com.alibaba.middleware.race.mom.encode.KryoEncoder;
import com.alibaba.middleware.race.mom.encode.KryoPool;

import java.io.IOException;

/**
 * Created by ivanmerlin on 2015/8/5.
 */

/**extends ChannelInboundHandlerAdapter 改成implements ChannelHandler？*/
public class ProducerConection extends ChannelInboundHandlerAdapter{
    private static String brokerIp;
    //TODO 设置为枚举类型
    public static final String TYPE="producer";
    public static final int PORT = 9999;
    static KryoPool pool=new KryoPool();

    private  volatile SendResult sendResult;

    private volatile Throwable exception;

    private Channel channel;

    private boolean connected;

    public static void setBrokerIp(String brokerIp) {
        ProducerConection.brokerIp = brokerIp;
    }



    public SendResult sendMessage(Message message) throws Throwable{
        message = preProcessMsg(message);
        /**如果处于非连接状态*/
        if(!connected){
            throw new IllegalStateException("not connected");
        }
        /**发送Message*/
        ChannelFuture channelFuture = channel.write(message);
        /**如果没有成功接收到消息？*/
        if(!channelFuture.awaitUninterruptibly().isSuccess()){
            close();
        }

        /**死锁知道有消息返回？*/
        waitForReponse();

        Throwable ex = exception;
        SendResult result = this.sendResult;
        this.sendResult = null;
        /**如果存在异常*/
        if(null != ex){
            close();
            throw ex;
        }
        return result;

    }

    /**如果没有收到消息就锁死当前channel*/
    private void waitForReponse(){
        synchronized (channel) {
            try{
                channel.wait();
            }catch(InterruptedException e){

            }
        }
    }

    /**
     * 异步callback发送消息，当前线程不阻塞。broker返回ack后，触发callback
     * @param message
     * @param callback
     */
    public void asyncSendMessage(Message message, SendCallback callback) {
        message = preProcessMsg(message);
        /**如果处于非连接状态*/
        if(!connected){
            throw new IllegalStateException("not connected");
        }
        /**发送Message*/
        ChannelFuture channelFuture = channel.write(message);
        /**如果没有成功接收到消息？*/
        /**异步要怎么取得结果*/
        //TODO 异步的情况下取得sendResult
        callback.onResult(sendResult);
    }


    public void connect() throws Throwable{
        /**加入TYPE:producer属性*/
        SendResult result=null;
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).remoteAddress(brokerIp,PORT)
                    .option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    /**增加编码解码器*/
                    socketChannel.pipeline().addLast(new KryoDecoder(pool));
                    socketChannel.pipeline().addLast(new KryoEncoder(pool));
                    socketChannel.pipeline().addLast(this);
                }
            });

            ChannelFuture channelFuture = bootstrap.connect(brokerIp,PORT).sync();

            /**不明白加这一步是什么意思 不知道该加不加*/
//            f.channel().closeFuture().sync();
            /***/
            if (!channelFuture.awaitUninterruptibly().isSuccess()){
                throw new Exception("connect fail");
            }

            channel = channelFuture.channel();
            connected = true;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void close(){

        connected=false;
        /**
         * 释放资源
         * 关闭channel
         */
        if (null != channel) {
            channel.close().awaitUninterruptibly();

            this.exception = new IOException("connection closed");
            synchronized (channel) {
                channel.notifyAll();
            }
            channel = null;
        }
    }


    private static Message preProcessMsg(Message message) {

        message.setProperty("type", TYPE);
        return message;
    }

    public boolean isClosed(){
        return (null == channel) || !channel.isActive()
                 || !channel.isWritable();
    }


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

            sendResult=(SendResult)msg;
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
