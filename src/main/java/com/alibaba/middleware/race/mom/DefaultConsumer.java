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
     * ���������ߣ���ʼ���ײ���Դ��Ҫ���������úͶ��Ĳ�������֮��ִ��
     */
    String topic;
    String groupId;
    String userId;
    MessageListener listener;

    public void start() {
        setGroupId("consumer");
//        String brokerIp=System.getProperty("SIP");
        String brokerIp = "127.0.0.1";
        ConsumerProxy.setBrokerIp(brokerIp);

    }

    /**
     * �����Ĳ���
     *
     * @param topic    ֻ���ܸ�topic����Ϣ
     * @param filter   ���Թ������������� area=hz����ʾֻ����area����Ϊhz����Ϣ����Ϣ�Ĺ���Ҫ�ڷ���˽���
     * @param listener
     */
    public void subscribe(String topic, String filter, MessageListener listener) {
        Message message = new Message();
        message.setProperty("topic", topic);
        /*
        ����filter
         */
        if(StringUtils.isNotBlank(filter)){
            String[] conditions=filter.split("=");
            if(conditions.length>1){
                message.setProperty(conditions[0],conditions[1]);
            }
        }
        message.setProperty("listener", listener.getClass().getName());
        message.setProperty("function", "consumerSubscribe");
        ConsumerProxy.sendMessage(message);
    }

    /**
     * ������������id��brokerͨ�����id��ʶ�������߻���
     *
     * @param groupId
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * ֹͣ�����ߣ�broker����Ͷ����Ϣ���������߻�����
     */
    public void stop() {
        Message message=new Message();
        message.setProperty("function","consumerStop");
        ConsumerProxy.sendMessage(message);
    }

    public static void main(String[] args) {


        TestThread thread = new TestThread();
        final int length=1;
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
