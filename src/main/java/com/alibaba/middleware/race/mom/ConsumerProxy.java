package com.alibaba.middleware.race.mom;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by ivan.wang on 2015/8/5.
 * ���︺����broker����ϵ �������� ��������
 */
public class ConsumerProxy {
    public static final int PORT = 9999;
    public static final int LISTEN_PORT = 9999;
    static String brokerIp;
    public static final String TYPE="consumer";
    public static boolean sendMessage(Message message){
        message=preProcessMsg(message);
        Socket socket = null;
        try {
            socket = new Socket(brokerIp,PORT);
            System.out.println("connect to server");
            /*
            考虑优化：传输处理过的信息 不使用java的序列化
             */
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
        ����message����ѽ
         */
        message.setProperty("type",TYPE);
        return message;
    }
    public static void setBrokerIp(String brokerIp) {
        ConsumerProxy.brokerIp = brokerIp;
    }


}
