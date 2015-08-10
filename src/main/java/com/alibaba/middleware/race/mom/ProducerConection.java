package com.alibaba.middleware.race.mom;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import com.alibaba.middleware.race.mom.encode.KryoDecoder;
import com.alibaba.middleware.race.mom.encode.KryoEncoder;
import com.alibaba.middleware.race.mom.encode.KryoPool;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.IOException;

/**
 * Created by ivanmerlin on 2015/8/5.
 */

/**extends ChannelInboundHandlerAdapter 改成implements ChannelHandler？*/
public class ProducerConection extends ChannelInboundHandlerAdapter{
    private static String brokerIp = "127.0.0.1";
    //TODO 设置为枚举类型
    public static final String TYPE="producer";
    public static final int PORT = 9999;
    static KryoPool pool=new KryoPool();

    private  volatile SendResult sendResult;

    private volatile Throwable exception;

    private Channel channel;

    private boolean connected;

    public void setBrokerIp(String brokerIp) {
        this.brokerIp = brokerIp;
    }



    public SendResult sendMessage(Message message) throws Throwable{

        message = preProcessMsg(message);
        /**如果处于非连接状态*/
        if(!connected){
            throw new IllegalStateException("not connected");
        }
        /**发送Message*/
        ChannelFuture channelFuture = channel.writeAndFlush(message);
        System.out.println("发送信息！！");

        /**阻塞线程直到写操作完成*/
        if(!channelFuture.awaitUninterruptibly().isSuccess()){
            close();
        }

        /**因为是同步调用所以锁死线程*/
        waitForReponse();

        /**如果存在异常就抛出*/
        Throwable ex = exception;
        exception = null;
        if(ex != null){
            throw ex;
        }

        SendResult result = this.sendResult;
        this.sendResult = null;
        return result;

    }

    /**锁死当前channel直到收到信息后*/
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
        ChannelFuture channelFuture = channel.writeAndFlush(message);
        /**如果没有成功接收到消息？*/
        /**异步要怎么取得结果*/
        //TODO 异步的情况下取得sendResult
        channelFuture.addListener(future -> callback.onResult(sendResult));


    }


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
                    socketChannel.pipeline().addLast(ProducerConection.this);
                }
            });

            ChannelFuture channelFuture = bootstrap.connect().sync();

            /**不明白加这一步是什么意思 不知道该加不加*/
//            f.channel().closeFuture().sync();
            /***/
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

    public void close(){

        connected=false;
        /**
         * 释放资源
         * 关闭channel
         */
        if (null != channel) {
            channel.close().awaitUninterruptibly();
            //TODO 还有其它资源需要释放吗？
            channel.eventLoop().shutdownGracefully();
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

        if(msg instanceof SendResult){
            /**解除channel的死锁状态*/
            synchronized (this.channel) {
                this.channel.notifyAll();
            }
            /**得到返回的结果*/
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
