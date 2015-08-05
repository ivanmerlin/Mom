package com.alibaba.middleware.race.mom;

import com.alibaba.middleware.race.mom.bean.ConsumeResult;
import com.alibaba.middleware.race.mom.bean.Message;
import com.alibaba.middleware.race.mom.service.MessageListener;
import com.alibaba.middleware.race.mom.utils.StringUtils;
import test.TestThread;

/**
 * Created by ivan.wang on 2015/8/5.
 */
public class DefaultConsumer implements Consumer {

    /**
     * 启动消费者，初始化底层资源。要在属性设置和订阅操作发起之后执行
     */
    String topic;
    String groupId;
    String userId;
    MessageListener listener;

    public void start() {
//        String brokerIp=System.getProperty("SIP");
        String brokerIp = "127.0.0.1";
        ConsumerProxy.setBrokerIp(brokerIp);

    }

    /**
     * 发起订阅操作
     *
     * @param topic    只接受该topic的消息
     * @param filter   属性过滤条件，例如 area=hz，表示只接受area属性为hz的消息。消息的过滤要在服务端进行
     * @param listener
     */
    public void subscribe(String topic, String filter, MessageListener listener) {
        Message message = new Message();
        message.setProperty("topic", topic);
        /*
        设置filter
         */
        if(StringUtils.isNotBlank(filter)){
            String[] conditions=filter.split("=");
            if(conditions.length>1){
                message.setProperty(conditions[0],conditions[1]);
            }
        }
        message.setProperty("groupId",groupId);
        message.setProperty("function", "consumerSubscribe");
        ConsumerProxy.sendMessage(message);
    }

    /**
     * 设置消费者组id，broker通过这个id来识别消费者机器
     *
     * @param groupId
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * 停止消费者，broker不再投递消息给此消费者机器。
     */
    public void stop() {
        Message message=new Message();
        message.setProperty("function","consumerStop");
        ConsumerProxy.sendMessage(message);
    }

    public static void main(String[] args) {


        TestThread thread = new TestThread();
        final int length=5;
        Thread[] ts=new Thread[length];
        for(int i=0;i<length;i++){
            Thread t=new Thread(thread);
            ts[i]=t;
        }
        for(int i=0;i<length;i++){
            ts[i].start();
        }

    }

}
