package com.alibaba.middleware.race.mom.broker;

import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ivanmerlin on 2015/8/5.
 */
public class GroupInfo {

    private String groupId;
    private Set<String> topicSet;
    private String condition;
    private Socket socket;

    public void addTopic(String topic){
        if(topicSet==null){
            topicSet=new HashSet<String>();
        }
        topicSet.add(topic);
    }
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }



    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Set<String> getTopicSet() {
        return topicSet;
    }

    public void setTopicSet(Set<String> topicSet) {
        this.topicSet = topicSet;
    }



    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
