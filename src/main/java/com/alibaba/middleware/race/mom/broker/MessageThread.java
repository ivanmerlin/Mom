package com.alibaba.middleware.race.mom.broker;

import com.alibaba.middleware.race.mom.Message;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by ivan.wang on 2015/8/6.
 */
public class MessageThread  extends Thread{

    String messageId;
   public MessageThread(String messageId){
       this.messageId=messageId;
   }

    @Override
    public void run() {
        Message message=MessageCenter.contentMap.get(messageId);
        String topic=message.getTopic();
        Set consumerSet=Registry.topicMap.get(topic);
        Iterator iterator=consumerSet.iterator();
        while(iterator.hasNext()){
            String groupId= (String) iterator.next();


        }

    }
}
