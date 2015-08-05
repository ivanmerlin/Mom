package com.alibaba.middleware.race.mom.broker;

import com.alibaba.middleware.race.mom.bean.Message;

import java.net.Socket;
import java.util.*;

/**
 * Created by ivan.wang on 2015/8/5.
 */
public class Registry {

    /*
    ��Ҫ����ģ�
    groupId ����
    ÿ���˵�messageId
    ÿ���û���IP
    ���û�����һ��Ψһ��IP
    ������Ϣ�п�
    --------------------
    Ҫ������ʱ�޷����������
    ���淵�ص���Ϣ
    ���û������յ�ʱɾ����

     */
    static Map<String, Set<String>> topicMap;
    static Map<String, GroupInfo> groupInfoMap;

    public Registry() {
        topicMap = new HashMap<String, Set<String>>();
    }

    /*
    �������ר����һ��DataObject beanMapper �������ݱ�¶
     */
    public static void registerSubscriber(Message message) {
        String topic=message.getProperty("topic");
        String groupId=message.getProperty("groupId");
        if (topicMap.containsKey(topic)) {
            topicMap.get(topic).add(groupId);
        } else {
            Set set = new HashSet();
            set.add(groupId);
            topicMap.put(topic, set);
        }
        if(groupInfoMap.containsKey(groupId)){
            GroupInfo info=groupInfoMap.get(groupId);
            info.getTopicSet().add(topic);
        }
    }

    public static void stopSubscribe(String groupId) {
        Set set=groupInfoMap.get(groupId).getTopicSet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String topic = it.next();
            topicMap.get(topic).remove(groupId);
        }
    }

    public static void main(String[] args) {
        GroupInfo info =new GroupInfo();
        info.setCondition("hi");
        Map<String,GroupInfo> map=new HashMap();
        map.put("hi", info);
        GroupInfo info2=map.get("hi");
        info2.setCondition("la");
        System.out.println("map.get(\"hi\").getCondition() = " + map.get("hi").getCondition());
        info2.setCondition("lala");
        System.out.println("map.get(\"hi\").getCondition() = " + map.get("hi").getCondition());
        info2=null;
        System.out.println("map.get(\"hi\").getCondition() = " + map.get("hi").getCondition());
    }
}
