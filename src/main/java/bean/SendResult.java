package bean;

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
    public Enum.SendStatus getStatus() {
        return status;
    }
    public void setStatus(Enum.SendStatus status) {
        this.status = status;
    }
    public String getMsgId() {
        return msgId;
    }
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
    private String info;
    private Enum.SendStatus status;
    private String msgId;
    @Override
    public String toString(){
        return "msg "+msgId+"  send "+(status== Enum.SendStatus.SUCCESS?"success":"fail")+"   info:"+info;
    }

}
