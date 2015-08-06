package com.alibaba.middleware.race.mom.utils;


import com.alibaba.middleware.race.mom.Message;

/**
 * Created by ivan.wang on 2015/8/5.
 */
public class MessageDispatcher {

   public static void dispatch(Message message){
      String function=message.getProperty("function");
       if(function.equals("consumerSubscribe")){
           MessageProcessor.consumerSubscribe(message);
       }else if(function.equals("consumerStop")){
           MessageProcessor.consumerStop(message);
       }

   }


}
