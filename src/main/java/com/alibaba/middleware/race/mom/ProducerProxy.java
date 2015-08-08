package com.alibaba.middleware.race.mom;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import com.alibaba.middleware.race.mom.encode.KryoDecoder;
import com.alibaba.middleware.race.mom.encode.KryoEncoder;
import com.alibaba.middleware.race.mom.encode.KryoPool;

/**
 * Created by ivanmerlin on 2015/8/5.
 */
public class ProducerProxy {
    private static String brokerIp;
    public static final String TYPE="producer";
    public static final int PORT = 9999;
    static KryoPool pool=new KryoPool();

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
                    .option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    /**增加编码解码器*/
                    socketChannel.pipeline().addLast(new KryoDecoder(pool));
                    socketChannel.pipeline().addLast(new KryoEncoder(pool));
                    socketChannel.pipeline().addLast(new MomProducerHandler());
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
