package com.alibaba.middleware.race.mom.broker;

import com.alibaba.middleware.race.mom.bean.Message;
import com.alibaba.middleware.race.mom.utils.MessageDispatcher;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by ivan.wang on 2015/8/5.
 */
public class ListenThread extends Thread{
    Socket socket;
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
            MessageDispatcher.dispatch(message);
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
