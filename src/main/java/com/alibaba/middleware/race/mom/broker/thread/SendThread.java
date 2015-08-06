package com.alibaba.middleware.race.mom.broker.thread;

import com.alibaba.middleware.race.mom.Message;
import com.alibaba.middleware.race.mom.broker.GroupInfo;
import com.alibaba.middleware.race.mom.broker.Registry;

/**
 * Created by ivan.wang on 2015/8/6.
 */
public class SendThread implements Runnable{
    Message message;
    String groupId;
    public SendThread(Message message,String groupId){
        this.groupId=groupId;
        this.message=message;
    }
    @Override
    public void run() {
        GroupInfo info= Registry.groupInfoMap.get(groupId);


    }
}
