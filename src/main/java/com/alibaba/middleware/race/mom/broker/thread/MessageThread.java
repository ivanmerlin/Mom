package com.alibaba.middleware.race.mom.broker.thread;

import com.alibaba.middleware.race.mom.DefaultConsumer;
import com.alibaba.middleware.race.mom.Message;
import com.alibaba.middleware.race.mom.broker.MessageCenter;
import com.alibaba.middleware.race.mom.broker.Registry;

import java.util.Set;

/**
 * Created by ivan.wang on 2015/8/6.
 */
public class MessageThread extends Thread{
    String messageId;

    @Override
    public void run() {
        Set<String> set=Registry.topicMap.get(messageId);
        for(String groupId:set){
            SendThread thread=new SendThread(messageId,groupId);
            thread.start();
        }

    }

    public MessageThread(){
        messageId=MessageCenter.queue.poll() ;
    }



}
