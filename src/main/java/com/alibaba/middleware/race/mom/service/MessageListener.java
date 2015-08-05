package com.alibaba.middleware.race.mom.service;

import com.alibaba.middleware.race.mom.bean.ConsumeResult;
import com.alibaba.middleware.race.mom.bean.Message;

/**
 * Created by ivan.wang on 2015/8/5.
 */
public interface MessageListener {
    /**
     *
     * @param message
     * @return
     */
    ConsumeResult onMessage(Message message);
}
