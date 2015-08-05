package com.alibaba.middleware.race.mom;

import com.alibaba.middleware.race.mom.bean.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by ivan.wang on 2015/8/5.
 * 这里负责与broker的联系 具体网络 发送请求
 */
public class ConsumerProxy {
    public static final int PORT = 12345;
    static String brokerIp;
    public static boolean sendMessage(Message message){
        message=preProcessMsg(message);
        Socket socket = null;
        try {
            socket = new Socket(brokerIp,PORT);
            System.out.println("connect to server");
            ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(message);
            out.close();
            System.out.println("trans over");
            socket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;
    }

    public static Message preProcessMsg(Message message){

        /*
        处理message内容呀
         */

        return message;
    }
    public static void setBrokerIp(String brokerIp) {
        ConsumerProxy.brokerIp = brokerIp;
    }
}
