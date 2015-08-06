package com.alibaba.middleware.race.mom.broker;


import com.alibaba.middleware.race.mom.Message;
import com.alibaba.middleware.race.mom.SendResult;
import com.alibaba.middleware.race.mom.SendStatus;
import com.alibaba.middleware.race.mom.utils.MessageDispatcher;

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
            ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
            Message message= null;
            message = (Message) in.readObject();
            System.out.println("message=" + message.getProperty("function"));
            if(message.getProperty("type").equals(CONSUMER_TYPE)){
                MessageDispatcher.dispatch(message);
            }else if(message.getProperty("type").equals(PRODUCER_TYPE)){
                SendResult result=new SendResult();
                ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
                System.out.println("message = " + message.getBody().toString());
                result.setStatus(SendStatus.SUCCESS);
                //generate a message Id
                result.setMsgId(message.getMsgId());
                out.writeObject(result);
                out.flush();
                out.close();
            }

            InetAddress addr=socket.getInetAddress();
            System.out.println(addr.getHostAddress());
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
