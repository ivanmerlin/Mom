package com.alibaba.middleware.race.mom.broker;

import com.alibaba.middleware.race.mom.bean.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * Created by ivanmerlin on 2015/8/5.
 */
public class MessageCenter {

    static Map<String,Queue<String>> topicMap=new HashMap<String, Queue<String>>();
    static Map<String,byte[]> contentMap=new HashMap<String, byte[]>();
    public static void add(Message message){
        //generate a messageId
        topicMap.get(message.getTopic()).offer(message.getMsgId());
        contentMap.put(message.getMsgId(),message.getBody());

    }

}