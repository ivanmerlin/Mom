package com.alibaba.middleware.race.mom.bean;

import java.io.Serializable;

/**
 * Created by ivan.wang on 2015/8/5.
 */

public class SendResult implements Serializable{
    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public enums.SendStatus getStatus() {
        return status;
    }
    public void setStatus(enums.SendStatus status) {
        this.status = status;
    }
    public String getMsgId() {
        return msgId;
    }
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
    private String info;
    private enums.SendStatus status;
    private String msgId;
    @Override
    public String toString(){
        return "msg "+msgId+"  send "+(status== enums.SendStatus.SUCCESS?"success":"fail")+"   info:"+info;
    }

}
