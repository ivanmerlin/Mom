package com.alibaba.middleware.race.mom.utils;

import com.alibaba.middleware.race.mom.Message;
import com.alibaba.middleware.race.mom.broker.Registry;

/**
 * Created by ivan.wang on 2015/8/5.
 */
public class MessageProcessor {


    public static void consumerSubscribe(Message message){
        System.out.println("invoke consumerSubscribe()");
        Registry.registerSubscriber(message);
    }
    public static void consumerStop(Message message){
        System.out.println("invoke consumerStop()");
        Registry.stopSubscribe(message.getProperty("groupId"));
    }


}
