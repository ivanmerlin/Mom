package com.alibaba.middleware.race.mom.broker.thread;

import com.alibaba.middleware.race.mom.Message;
import com.alibaba.middleware.race.mom.broker.GroupInfo;
import com.alibaba.middleware.race.mom.broker.MessageCenter;
import com.alibaba.middleware.race.mom.broker.Registry;

/**
 * Created by ivan.wang on 2015/8/6.
 */
public class SendThread extends Thread{
    Message message;
    String groupId;
    public SendThread(String messageId,String groupId){
        this.groupId=groupId;
        this.message= MessageCenter.contentMap.get(messageId);
    }
    @Override
    public void run() {
        GroupInfo info= Registry.groupInfoMap.get(groupId);


    }
}
