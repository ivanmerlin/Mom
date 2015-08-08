package com.alibaba.middleware.race.mom;


import com.alibaba.middleware.race.mom.encode.KryoDecoder;
import com.alibaba.middleware.race.mom.encode.KryoEncoder;
import com.alibaba.middleware.race.mom.encode.KryoPool;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


public class ConsumerConnection {
    public static final int PORT = 9999;
    public static final int LISTEN_PORT = 9999;
    static String brokerIp;
    public static final String TYPE="consumer";
    static KryoPool pool=new KryoPool();

    public static boolean sendMessage(Message message){
        final Message message_;
        /**加入消费者属性*/
        message_=preProcessMsg(message);
//        Socket socket = null;
//        try {
//            socket = new Socket(brokerIp,PORT);
//            System.out.println("connect to server");
//            /*
//            考虑优化：传输处理过的信息 不使用java的序列化
//             */
//            ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
//            out.writeObject(message);
//            out.close();
//            System.out.println("trans over");
//            socket.close();
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).remoteAddress(brokerIp,PORT)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            /**增加编码解码器*/
                            socketChannel.pipeline().addLast(new KryoDecoder(pool));
                            socketChannel.pipeline().addLast(new KryoEncoder(pool));
                            socketChannel.pipeline().addLast(new MomConsumerHandler(message_));
                        }
                    });

        }finally {
            group.shutdownGracefully();
        }

        return false;
    }

    public static Message preProcessMsg(Message message){
        message.setProperty("type",TYPE);
        return message;
    }
    public static void setBrokerIp(String brokerIp) {
        ConsumerConnection.brokerIp = brokerIp;
    }


}
