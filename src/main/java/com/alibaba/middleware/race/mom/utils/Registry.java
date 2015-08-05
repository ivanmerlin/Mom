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
    ��Ҫ����ģ�
    groupId ����
    ÿ���˵�messageId
    ÿ���û���IP
    ���û�����һ��Ψһ��IP
    ������Ϣ�п�
    --------------------
    Ҫ������ʱ�޷����������
    ���淵�ص���Ϣ
    ���û������յ�ʱɾ����

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
