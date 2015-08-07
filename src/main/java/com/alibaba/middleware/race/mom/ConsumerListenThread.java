package com.alibaba.middleware.race.mom;

import com.alibaba.middleware.race.mom.broker.MessageThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ivan.wang on 2015/8/7.
 */
public class ConsumerListenThread extends Thread{
    private boolean keep;
    private final String ACK_MESSAGE="ACK";
    Socket socket;
    public ConsumerListenThread(Socket socket){
        keep=true;
        this.socket=socket;
    }

    @Override
    public void run() {
        while (keep){
            try {
                ObjectInputStream input=new ObjectInputStream(socket.getInputStream());
                Message m= (Message) input.readObject();
                ObjectOutputStream output=new ObjectOutputStream(socket.getOutputStream());
                output.writeUTF(ACK_MESSAGE);
                output.flush();
                //不确定可不可以关掉流
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void setKeep(boolean keep) {
        this.keep = keep;
    }
}
