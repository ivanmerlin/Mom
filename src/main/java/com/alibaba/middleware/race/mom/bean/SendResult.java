package com.alibaba.middleware.race.mom.bean;

/**
 * Created by ivan.wang on 2015/8/5.
 */

public class SendResult {
    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public com.alibaba.middleware.race.mom.enums.SendStatus getStatus() {
        return status;
    }
    public void setStatus(com.alibaba.middleware.race.mom.enums.SendStatus status) {
        this.status = status;
    }
    public String getMsgId() {
        return msgId;
    }
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
    private String info;
    private com.alibaba.middleware.race.mom.enums.SendStatus status;
    private String msgId;
    @Override
    public String toString(){
        return "msg "+msgId+"  send "+(status== com.alibaba.middleware.race.mom.enums.SendStatus.SUCCESS?"success":"fail")+"   info:"+info;
    }

}
