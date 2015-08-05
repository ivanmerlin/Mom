package com.alibaba.middleware.race.mom;

import com.alibaba.middleware.race.mom.bean.Message;
import com.alibaba.middleware.race.mom.bean.SendResult;
import com.alibaba.middleware.race.mom.service.SendCallback;

/**
 * Created by ivan.wang on 2015/8/5.
 */
public class DefaultProducer implements Producer{
    public void start() {

    }

    public void setTopic(String topic) {

    }

    public void setGroupId(String groupId) {

    }

    public SendResult sendMessage(Message message) {
        return null;
    }

    public void asyncSendMessage(Message message, SendCallback callback) {

    }

    public void stop() {

    }
}
