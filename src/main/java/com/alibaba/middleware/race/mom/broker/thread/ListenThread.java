package com.alibaba.middleware.race.mom.broker.thread;


import com.alibaba.middleware.race.mom.Message;
import com.alibaba.middleware.race.mom.SendResult;
import com.alibaba.middleware.race.mom.SendStatus;
import com.alibaba.middleware.race.mom.utils.MessageDispatcher;
import com.alibaba.middleware.race.mom.utils.NetStreamUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by ivan.wang on 2015/8/5.
 */
public class ListenThread extends Thread{
    Socket socket;
    private static final String CONSUMER_TYPE="consumer";
    private static final String PRODUCER_TYPE="producer";
    public ListenThread(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("start listen thread");
            NetStreamUtils netUtils=new NetStreamUtils(socket);
            Message message= null;
            message = (Message) netUtils.readObject();
            System.out.println("message=" + message.getProperty("function"));
            if(message.getProperty("type").equals(CONSUMER_TYPE)){
                MessageDispatcher.dispatch(message,socket);
            }else if(message.getProperty("type").equals(PRODUCER_TYPE)){
                System.out.println("message = " + message.getBody().toString());
                //generate a message Id

            }


        } finally {

        }
    }
}
