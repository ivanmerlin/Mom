package com.alibaba.middleware.race.mom.broker;


import com.alibaba.middleware.race.mom.Message;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ivanmerlin on 2015/8/5.
 */
public class MessageCenter {
    /*
    @param topicMap 用于维护每一个topic的消息队列 没想好要不要用
     */
    public static Map<String,Queue<String>> topicMap=new HashMap<String, Queue<String>>();
    /*
    @param contentMap 用于根据消息ID映射得到对应的消息体
     */
    public static Map<String,Message> contentMap=new HashMap<String, Message>();
    /*
    @param queue 维护实际的消息队列
     */
    public static Queue<String> queue=new LinkedBlockingQueue();
    /*
    @param idSet 用于判断产生的消息id是否已经存在 保证ID的唯一
     */
    public static Set<String> idSet=new HashSet();
    /*
    @method add 接收producer发来的消息内容
    产生唯一的MSGID
    将其保存在对应的map和队列里
     */
    public static void add(Message message){
        //generate a messageId
        String messageId=generateId();
        message.setMsgId(messageId);
        topicMap.get(message.getTopic()).offer(messageId);
        contentMap.put(messageId, message);
        queue.offer(messageId);
        for(Map.Entry<String,Message> entry:contentMap.entrySet()){
            System.out.println("entry.getKey() = " + entry.getKey());
            System.out.println("entry.getValue() = " + entry.getValue());
        }
        for(String s:queue){
            System.out.println("s = " + s);
        }
        for(String id:idSet){
            System.out.println("id = " + id);
        }
    }

    /*
    产生一个八位的消息ID 范围在10000000~90000000
     */
    public static String generateId(){
        Random random=new Random();
        String result=Integer.toString(random.nextInt(90000000)+10000000);
        while(idSet.contains(result)){
            result=Integer.toString(random.nextInt(90000000)+10000000);
        }
        return  result;
    }

    public static void main(String[] args) {
        System.out.println(generateId());
    }

}
