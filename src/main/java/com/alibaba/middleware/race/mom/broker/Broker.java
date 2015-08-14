package com.alibaba.middleware.race.mom.broker;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.middleware.race.mom.encode.KryoDecoder;
import com.alibaba.middleware.race.mom.encode.KryoEncoder;
import com.alibaba.middleware.race.mom.encode.KryoPool;

/**
 * Created by ivan.wang on 2015/8/5.
 * 1 把MOM的api加到你的工程pom依赖里面，你的功能要实现两个部分，一个是服务端broker，还有客户端接口com.alibaba.m
 * iddleware.race.mom.Producer和com.alibaba.middleware.race.mom.Consumer，对于实现的类名我们也固定好了，分别为c
 * om.alibaba.middleware.race.mom.DefaultProducer和com.alibaba.middleware.race.mom.DefaultConsumer。
 * <p/>
 * 2 如何使用测试工程http://code.taobao.org/p/race/src/trunk/momtest/。 下载测试工程后， 你会看到一堆测试用例，
 * 还有两个类分别是com.alibaba.middleware.race.mom.DefaultProducer和com.alibaba.middleware.race.mom.
 * DefaultConsumer，这两个类是空实现，放在这里仅仅是为了不让测试工程出现编译错误。实际上这两个类是你要实现的，
 * 要放到你自己的工程里面，然后把测试工程的类干掉。等你实现好之后，让测试工程依赖你的工程就可以开始进行自测了。
 * <p/>
 * 3 我们如何自动跑分。 参赛者要自己写build.sh, 把自己实现的类和依赖的jar都打到 ./target/lib下面；
 * 除此之外，mom还要写一个broker.sh,这个脚本主要是用来启动broker的。我们在跑自动化测试的时候，
 * 会先运行build.sh,再运行broker.sh,启动broker。broker启动成功后，运行测试类，指定classpath为
 * ./target/lib。运行测试类的时候我们会传java的系统启动参数 -DSIP指定broker的ip，DefaultProducer
 * 和DefaultConsumer要获取这个ip去和broker建立链接。
 * <p/>
 * 4 如果消费失败，如何进行重投。 这个主要是体现mom的削峰填谷能力的，在消费方恢复正常之后，
 * mom能够在不影响发送方的消息发送和消息的首次投递基础上，尽快的重投给之前消费失败的订阅组。打个比方，
 * mom能抗12000的tps，在峰值时刻消息发送和投递的tps分别都为10000，那么这个时候可以腾出2000的能力来重投；
 * 峰值过后，tps降为5000，这个时候mom就要有填谷的能力了，可以腾出7000的能力进行消息重投，
 * 尽快把堆积的消息投递给消费者。
 * <p/>
 * 5 压测的broker是机械硬盘
 * <p/>
 * 6 消费订阅的属性过滤是要在服务端过滤，不满足过滤条件的消息不能推到客户端。
 * <p/>
 * 7 消息处理流程描述，定义 生产者=P，消费者=C，broker=B。 P发送消息给B，B把消息持久化后，返回ack给P，
 * 告诉P发送的结果，成功或者失败；B把消息持久化之后，匹配订阅关系，找出符合条件的C（一个消息可能会有多个不同的
 * C订阅），然后把消息推送给所有符合条件的C其中一台机器（C可能是个集群，同样的groupid），C收到消息后进行消费逻辑
 * 的处理，并把处理结果成功或者失败ack返回给B。B如果收到C的成功ack之后，那么这条消息就算消费成功了；如果C是失败
 * 的ack，那么还得尽快给C重新投递；如果在10秒内还没收到C的ack，那么认为消费失败，后面还得尽快重新投递给C。
 */
public class Broker {
    private static final int PORT = 9999;
    int connectNum;
    static KryoPool pool=new KryoPool();
    private ConfigServer configserver=new ConfigServer();
    
    
    public Broker() {
    }

    public void start(){

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                    .localAddress(PORT).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    /**添加Object解码器*/
//                    channel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())),new ObjectEncoder());
                    socketChannel.pipeline().addLast(new KryoDecoder(pool));
                    socketChannel.pipeline().addLast(new KryoEncoder(pool));
                    /**添加对消息处理的Handler*/
                    socketChannel.pipeline().addLast(new MomServerHandler(configserver));
                }
            });

            /**开启服务和关闭服务*/
            ChannelFuture channelFuture = serverBootstrap.bind().sync();

            System.out.println("Netty Server starts successfully!!");

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        Broker broker=new Broker();
        broker.start();

    }
}
