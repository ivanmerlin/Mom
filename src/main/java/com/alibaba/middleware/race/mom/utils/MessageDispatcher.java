package com.alibaba.middleware.race.mom.utils;


import com.alibaba.middleware.race.mom.Message;

import java.net.Socket;

/**
 * Created by ivan.wang on 2015/8/5.
 */
public class MessageDispatcher {

   public static void dispatch(Message message,Socket socket){
      String function=message.getProperty("function");
       System.out.println("function = " + function);
       if(function.equals("consumerSubscribe")){
           MessageProcessor.consumerSubscribe(message,socket);
       }else if(function.equals("consumerStop")){
           MessageProcessor.consumerStop(message);
       }

   }


}
