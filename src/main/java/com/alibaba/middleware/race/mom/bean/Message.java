package com.alibaba.middleware.race.mom.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivan.wang on 2015/8/5.
 */

public class Message implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 5295808332504208830L;
    private String topic;
    private byte[] body;
    //ȫ��Ψһ����Ϣid����ͬ��Ϣ�����ظ�
    private String msgId;
    private long bornTime;

    private Map<String, String> properties = new HashMap<String, String>();

    public void setTopic(String topic) {
        this.topic = topic;
    }
    public String getMsgId() {
        return msgId;
    }
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
    public String getTopic() {
        return topic;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }

    public String getProperty(String key) {
        return properties.get(key);
    }
    /**
     * ������Ϣ����
     * @param key
     * @param value
     */
    public void setProperty(String key, String value) {
        properties.put(key, value);
    }
    /**
     * ɾ����Ϣ����
     * @param key
     */
    public void removeProperty(String key) {
        properties.remove(key);
    }
    public long getBornTime() {
        return bornTime;
    }
    public void setBornTime(long bornTime) {
        this.bornTime = bornTime;
    }

}
