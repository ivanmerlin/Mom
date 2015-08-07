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

    public void start() {
//        String brokerIp=System.getProperty("SIP");
        String brokerIp = "127.0.0.1";
        ConsumerProxy.setBrokerIp(brokerIp);

    }

    @Override
    public void subscribe(String topic, String filter, com.alibaba.middleware.race.mom.MessageListener listener) {
        Message message = new Message();
        message.setProperty("topic", topic);
        /*
        璁剧疆filter
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
     * 发起订阅操作
     *
     * @param topic
     *            只接受该topic的消息
     * @param filter
     *            属性过滤条件，例如 area=hz，表示只接受area属性为hz的消息。消息的过滤要在服务端进行
     * @param listener
     */

    /**
     * 璁剧疆娑堣垂鑰呯粍id锛宐roker閫氳繃杩欎釜id鏉ヨ瘑鍒秷璐硅�呮満鍣�
     *
     * @param groupId
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void stop() {
        Message message=new Message();
        message.setProperty("function","consumerStop");
        ConsumerProxy.sendMessage(message);
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
