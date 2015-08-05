package com.alibaba.middleware.race.mom.utils;

import com.alibaba.middleware.race.mom.bean.Message;

/**
 * Created by ivan.wang on 2015/8/5.
 */
public class MessageDispatcher {

   public static Message dispatch(Message message){
      String function=message.getProperty("function");
       if(function.equals("consumerSubscribe")){
           return MessageProcessor.consumerSubscribe(message);
       }else if(function.equals("consumerStop")){
           return MessageProcessor.consumerStop(message);
       }
       return null;
   }


}
