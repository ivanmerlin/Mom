package com.alibaba.middleware.race.mom;


import com.alibaba.middleware.race.mom.utils.NetStreamUtils;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by ivan.wang on 2015/8/5.
 * ���︺����broker����ϵ �������� ��������
 */
public class ConsumerProxy {
    public static final int PORT = 9999;
    public static final int LISTEN_PORT = 8787;
    static String brokerIp;
    public static final String TYPE="consumer";
    public static ConsumerListenThread thread;
    public static Socket socket;
    public static boolean sendMessage(Message message){
        message=preProcessMsg(message);

        try {
            System.out.println("brokerIp = " + brokerIp);
            if(socket==null) {
                socket = new Socket(brokerIp, PORT);
            }
            System.out.println("connect to server");
            /*
            考虑优化：传输处理过的信息 不使用java的序列化
             */
            NetStreamUtils netUtils=new NetStreamUtils(socket);
            netUtils.writeObject(message);

            System.out.println("trans over");
            startListening(socket);
            System.out.println("try to start listen");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;
    }

    public static void startListening(Socket socket){
        thread=new ConsumerListenThread(socket);
        thread.start();
        System.out.println("consumer:start listening--");
    }
    public static Message preProcessMsg(Message message){
        message.setProperty("type",TYPE);
        return message;
    }
    public static void setBrokerIp(String brokerIp) {
        ConsumerProxy.brokerIp = brokerIp;
    }


}
