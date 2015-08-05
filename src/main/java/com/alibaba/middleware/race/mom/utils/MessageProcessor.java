package com.alibaba.middleware.race.mom.utils;

import com.alibaba.middleware.race.mom.bean.Message;

/**
 * Created by ivan.wang on 2015/8/5.
 */
public class MessageProcessor {


    public static Message consumerSubscribe(Message message){
        System.out.println("invoke consumerSubscribe()");
        return message;
    }
    public static Message consumerStop(Message message){
        System.out.println("invoke consumerStop()");
        return message;
    }


}
