package com.alibaba.middleware.race.mom.service;

import com.alibaba.middleware.race.mom.bean.SendResult;

/**
 * Created by ivan.wang on 2015/8/5.
 */
public interface SendCallback {
    void onResult(SendResult result);
}
