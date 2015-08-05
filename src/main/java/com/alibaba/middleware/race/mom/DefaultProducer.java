package com.alibaba.middleware.race.mom;

import com.alibaba.middleware.race.mom.bean.Message;
import com.alibaba.middleware.race.mom.bean.SendResult;
import com.alibaba.middleware.race.mom.service.SendCallback;
import enums.SendStatus;

/**
 * Created by ivan.wang on 2015/8/5.
 */
public class DefaultProducer implements Producer{

    String groupId;
    String topic;
    /**
     * 启动生产者，初始化底层资源。在所有属性设置完毕后，才能调用这个方法
     */

    public void start() {
//        String brokerIp=System.getProperty("SIP");
        String brokerIp = "127.0.0.1";
        ProducerProxy.setBrokerIp(brokerIp);
    }
    /**
     * 设置生产者可发送的topic
     * @param topic
     */
    public void setTopic(String topic) {
        this.topic=topic;
    }
    /**
     * 设置生产者id，broker通过这个id来识别生产者集群
     * @param groupId
     */
    public void setGroupId(String groupId) {
        this.groupId=groupId;
    }
    /**
     * 发送消息
     * @param message
     * @return
     */
    public SendResult sendMessage(Message message) {
        message.setTopic(topic);
        message.setProperty("groupId",groupId);
        return ProducerProxy.sendMessage(message);
    }
    /**
     * 异步callback发送消息，当前线程不阻塞。broker返回ack后，触发callback
     * @param message
     * @param callback
     */
    public void asyncSendMessage(Message message, SendCallback callback) {

    }
    /**
     * 停止生产者，销毁资源
     */
    public void stop() {

    }

    public static void main(String[] args) {
        Producer producer=new DefaultProducer();
        producer.setGroupId("PG-test");
        producer.setTopic("T-test");
        producer.start();
        Message message=new Message();
        message.setBody("Hello MOM".getBytes());
        message.setProperty("area", "us");
        SendResult result=producer.sendMessage(message);
        System.out.println("send msg");
        if (result.getStatus().equals(SendStatus.SUCCESS)) {
            System.out.println("send success:"+result.getMsgId());
        }
    }
}
