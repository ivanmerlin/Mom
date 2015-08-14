package com.alibaba.middleware.race.mom.broker;

import io.netty.channel.Channel;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.middleware.race.mom.Message;
import com.alibaba.middleware.race.mom.SendResult;
import com.alibaba.middleware.race.mom.SendStatus;


public class ConfigServer {

    Map<String, Set<String>> brokerMap;
    Map<String, Set<MomServerHandler>> groupRouter;
    
    
    public ConfigServer(){
        brokerMap=new ConcurrentHashMap<String, Set<String>>();
        groupRouter=new ConcurrentHashMap<String, Set<MomServerHandler>>();
        
    }
    
    
    public void setRegist(Message message,MomServerHandler channel){
        String topic=message.getTopic();
//        System.out.println(topic+"is reg topic");
        String groupId=message.getProperty("groupId");
        if(brokerMap.containsKey(topic)){
            brokerMap.get(topic).add(groupId);
        }else{
            HashSet<String> groupSet=new HashSet<String>();
            groupSet.add(groupId);
            brokerMap.put(topic, groupSet);
        }
        if(groupRouter.containsKey(groupId)){
            groupRouter.get(groupId).add(channel);
        }else{
            HashSet<MomServerHandler> chennelSet=new HashSet<MomServerHandler>();
            chennelSet.add(channel);
            groupRouter.put(groupId, chennelSet);
        }
    }
    
    public SendResult sendMsg(Message message){
        System.out.println(message.getTopic()+"is topic");
        Set<String> groupSet=brokerMap.get(message.getTopic());
        if(groupSet==null){
           //没有人订阅
            System.out.println("no one registed");
            return sendResult(message,false);
        }
        Iterator<String> i=groupSet.iterator();
        Random r=new Random();
        while(i.hasNext()){
            String groupId=i.next();
            Set<MomServerHandler> chennels=groupRouter.get(groupId);
            if(chennels.size()==0){continue;}
            MomServerHandler[] channelArr=chennels.toArray(new MomServerHandler[0]);
            MomServerHandler channel=channelArr[r.nextInt(channelArr.length)];
            channel.sendMsgToConsumer(message);
        }
        return sendResult(message,true);
    }
    
    private SendResult sendResult(Message message,boolean succ){
        SendResult sendResult = new SendResult();
        sendResult.setMsgId(message.getMsgId());
        sendResult.setStatus(succ?SendStatus.SUCCESS:SendStatus.FAIL);
        return sendResult;
    }
}
