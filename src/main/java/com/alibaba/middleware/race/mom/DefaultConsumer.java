package com.alibaba.middleware.race.mom;

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
    ConsumerConnection consumerConnection= new ConsumerConnection();
    Message subscribeMsg;

    public void start() {
        String brokerIp=System.getProperty("SIP");
        if(brokerIp==null){
            brokerIp = "127.0.0.1";
        }
        consumerConnection.setBrokerIp(brokerIp);
        try {
            consumerConnection.connect(subscribeMsg,listener);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void subscribe(String topic, String filter, com.alibaba.middleware.race.mom.MessageListener listener) {
        subscribeMsg = new Message();
        subscribeMsg.setTopic(topic);
        if(StringUtils.isNotBlank(filter)){
            String[] conditions=filter.split("=");
            if(conditions.length>1){
                subscribeMsg.setProperty(conditions[0],conditions[1]);
            }
        }
        subscribeMsg.setProperty("groupId",groupId);
        subscribeMsg.setProperty("function", "consumerSubscribe");
        subscribeMsg.setProperty("type", "consumer");
        this.listener=listener;
    }

    /**
     * 发起订阅操作
     *
     * @param topic
     *            只接受该topic的消息
     * @param filter
     *            属性过滤条件，例如 area=hz，表示只接受area属性为hz的消息。消息的过滤要在服务端进行
     * @param listener
     */


    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void stop() {
        Message message=new Message();
        message.setProperty("function", "consumerStop");
        consumerConnection.close();
//        consumerConnection.sendMessage(message);
    }

    public static void main(String[] args) {



        final int length=1;
        Thread[] ts=new Thread[length];

        for(int i=0;i<length;i++){
            ts[i]= new TestThread(i);
            ts[i].start();
        }

    }

}
