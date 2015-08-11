package com.alibaba.middleware.race.mom;

import com.alibaba.middleware.race.mom.broker.MessageThread;
import com.alibaba.middleware.race.mom.utils.NetStreamUtils;

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
                NetStreamUtils netUtils=new NetStreamUtils(socket);
                Message m= (Message) netUtils.readObject();
                netUtils.writeString(ACK_MESSAGE);
            System.out.println("MsgId:"+m.getMsgId()+"topic:"+m.getTopic()+" "+m.getBody());
        }

    }

    public void setKeep(boolean keep) {
        this.keep = keep;
    }
}
