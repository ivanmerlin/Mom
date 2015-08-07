package com.alibaba.middleware.race.mom.broker.thread;

import com.alibaba.middleware.race.mom.Message;
import com.alibaba.middleware.race.mom.broker.GroupInfo;
import com.alibaba.middleware.race.mom.broker.MessageCenter;
import com.alibaba.middleware.race.mom.broker.Registry;
import com.alibaba.middleware.race.mom.utils.NetStreamUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by ivan.wang on 2015/8/6.
 * 具体用来发送的线程
 */
public class SendThread extends Thread {
    Message message;
    String groupId;

    public SendThread(String messageId, String groupId) {
        this.groupId = groupId;
        this.message = MessageCenter.contentMap.get(messageId);
    }

    @Override
    public void run() {
        GroupInfo info = Registry.groupInfoMap.get(groupId);
        Socket socket = info.getSocket();
        NetStreamUtils netUtil = new NetStreamUtils(socket);
        /*
            没有想好在哪里保存到本地
         */
        /*
        在接受到成功回执前不停发送
         */
        String result = send(netUtil);
        while (result == null) {
            send(netUtil);
        }
    }

    public String send(NetStreamUtils netUtil) {

        netUtil.writeObject(message);
        String result = netUtil.readString();
        return result;
    }
}