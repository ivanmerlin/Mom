package com.alibaba.middleware.race.mom.utils;

import com.alibaba.middleware.race.mom.bean.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ivan.wang on 2015/8/5.
 */
public class Registry {

    /*
    需要管理的：
    groupId 分组
    每个人的messageId
    每个用户的IP
    给用户分配一个唯一的IP
    绑定在消息中咯
    --------------------
    要处理暂时无法发出的情况
    保存返回的消息
    当用户反馈收到时删除它

     */
   static Map<String,Set> groupMap;

    public Registry(){
        groupMap=new HashMap<String,Set>();


    }

    public static void onConnect(Message message){
        String groupId=message.getProperty("group");
        Set groupSet=groupMap.get(groupId);

    }


}
