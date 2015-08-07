package com.alibaba.middleware.race.mom.broker;


import com.alibaba.middleware.race.mom.Message;

import java.net.Socket;
import java.util.*;

/**
 * Created by ivan.wang on 2015/8/5.
 */
public class Registry {

    /*
    需要管理的：
    groupId 分组
    每个人的messageId
    每个用户的IP
    给用户分配一个唯一的IP
    绑定在消息中咯
    --------------------
    要处理暂时无法发出的情况
    保存返回的消息
    当用户反馈收到时删除它

     */
    public static Map<String, Set<String>> topicMap=new HashMap<String, Set<String>>();
    public static Map<String, GroupInfo> groupInfoMap=new HashMap<String, GroupInfo>();



    /*
    这里可以专门做一个DataObject beanMapper 减少数据暴露
     */
    public static void registerSubscriber(Message message,Socket socket) {
        String topic=message.getProperty("topic");
        String groupId=message.getProperty("groupId");
        /*
            将用户加到对应topic的订阅列表中。
         */
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
        }else {
            GroupInfo info=new GroupInfo();
            info.setSocket(socket);
            info.addTopic(topic);
            info.setGroupId(groupId);
            info.setCondition(message.getProperty("condition"));
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
