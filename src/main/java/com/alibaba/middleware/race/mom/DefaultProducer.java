package com.alibaba.middleware.race.mom;


/**
 * Created by ivan.wang on 2015/8/5.
 */
public class DefaultProducer  implements Producer {

    String groupId;
    String topic;
    ProducerConection producerConection;
    /**
     * 启动生产者，初始化底层资源。在所有属性设置完毕后，才能调用这个方法
     */

    public void start() {
        String brokerIp;
        try {
            brokerIp = System.getProperty("SIP");
        }catch (Exception e) {
            brokerIp = "127.0.0.1";
        }

        //TODO 可以创建一个
        producerConection = new ProducerConection();
        producerConection.setBrokerIp(brokerIp);

        try {
            /**开启服务器*/
            producerConection.connect();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
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
        try {
            return producerConection.sendMessage(message);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return null;
    }
    /**
     * 异步callback发送消息，当前线程不阻塞。broker返回ack后，触发callback
     * @param message
     * @param callback
     */
    public void asyncSendMessage(Message message, SendCallback callback) {
        producerConection.asyncSendMessage(message,callback);
    }
    /**
     * 停止生产者，销毁资源
     */
    public void stop() {
        producerConection.close();
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

    }
}
