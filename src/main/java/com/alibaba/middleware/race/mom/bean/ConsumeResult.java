package com.alibaba.middleware.race.mom.bean;

/**
 * Created by ivan.wang on 2015/8/5.
 */
public class ConsumeResult {
    private Enum.ConsumeStatus status= Enum.ConsumeStatus.FAIL;
    private String info;
    public void setStatus(Enum.ConsumeStatus status) {
        this.status = status;
    }
    public Enum.ConsumeStatus getStatus() {
        return status;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public String getInfo() {
        return info;
    }
}
