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
     * ���������ߣ���ʼ���ײ���Դ������������������Ϻ󣬲��ܵ����������
     */

    public void start() {
//        String brokerIp=System.getProperty("SIP");
        String brokerIp = "127.0.0.1";
        ProducerProxy.setBrokerIp(brokerIp);
    }
    /**
     * ���������߿ɷ��͵�topic
     * @param topic
     */
    public void setTopic(String topic) {
        this.topic=topic;
    }
    /**
     * ����������id��brokerͨ�����id��ʶ�������߼�Ⱥ
     * @param groupId
     */
    public void setGroupId(String groupId) {
        this.groupId=groupId;
    }
    /**
     * ������Ϣ
     * @param message
     * @return
     */
    public SendResult sendMessage(Message message) {
        message.setTopic(topic);
        message.setProperty("groupId",groupId);
        return ProducerProxy.sendMessage(message);
    }
    /**
     * �첽callback������Ϣ����ǰ�̲߳�������broker����ack�󣬴���callback
     * @param message
     * @param callback
     */
    public void asyncSendMessage(Message message, SendCallback callback) {

    }
    /**
     * ֹͣ�����ߣ�������Դ
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
