package com.alibaba.middleware.race.mom.broker;


import com.alibaba.middleware.race.mom.Message;

import java.util.*;

/**
 * Created by ivanmerlin on 2015/8/5.
 */
public class MessageCenter {

    static Map<String,Queue<String>> topicMap=new HashMap<String, Queue<String>>();
    static Map<String,Message> contentMap=new HashMap<String, Message>();
    static Queue queue;
    static int maxQueue;
    static Set<String> idSet=new HashSet();
    public static void add(Message message){
        //generate a messageId


        topicMap.get(message.getTopic()).offer(message.getMsgId());
        contentMap.put(message.getMsgId(),message);

    }

    private String generateId(){
        Random random=new Random();
        String result=Integer.toString(random.nextInt(90000000)+10000000);
        return  result;
    }

}
