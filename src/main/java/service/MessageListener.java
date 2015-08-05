package service;

import bean.ConsumeResult;
import bean.Message;

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
