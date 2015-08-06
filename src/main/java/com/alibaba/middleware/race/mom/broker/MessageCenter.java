package com.alibaba.middleware.race.mom.broker;


import com.alibaba.middleware.race.mom.Message;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ivanmerlin on 2015/8/5.
 */
public class MessageCenter {

    static Map<String,Queue<String>> topicMap=new HashMap<String, Queue<String>>();
    static Map<String,Message> contentMap=new HashMap<String, Message>();
    static Queue<String> queue=new LinkedBlockingQueue();
    static Set<String> idSet=new HashSet();
    public static void add(Message message){
        //generate a messageId
        String messageId=generateId();
        message.setMsgId(messageId);
        topicMap.get(message.getTopic()).offer(messageId);
        contentMap.put(messageId,message);
        queue.offer(messageId);
    }

    public static String generateId(){
        Random random=new Random();
        String result=Integer.toString(random.nextInt(90000000)+10000000);
        while(idSet.contains(result)){
            result=Integer.toString(random.nextInt(90000000)+10000000);
        }
        return  result;
    }



}
