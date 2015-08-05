package com.alibaba.middleware.race.mom.broker;

import java.util.Set;

/**
 * Created by ivanmerlin on 2015/8/5.
 */
public class GroupInfo {

    private String groupId;
    private Set<String> topicSet;
    private String hostAddress;
    private String condition;

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

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
