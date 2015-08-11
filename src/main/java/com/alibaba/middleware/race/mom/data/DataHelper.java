package com.alibaba.middleware.race.mom.data;


import com.alibaba.middleware.race.mom.Message;
import com.alibaba.middleware.race.mom.broker.Registry;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.*;

/**
 * Created by ivanmerlin on 2015/8/5.
 */
public class DataHelper {
    /*
    需要把消息队列保存在本地 防止数据丢失
    @String groupId  -->用户标识
    @String messageId
    @byte[] messageBody
    @boolean ack
     */
    /*
    这里又使用了java的序列化 肯定会影响性能吧
     */
    static BufferedOutputStream out;

    public static void saveGroupMessage(String groupId,Message message){
        String filePath;
        if(System.getProperty("userhome")!=null) {
            filePath = System.getProperty("userhome") + File.separator + "store" + "/" + groupId+"_"+ message.getMsgId() ;
        }else{
            filePath= "/userhome"+ File.separator + "store" + "/" + groupId+"_"+message.getMsgId() ;
        }
        try {
            File file=new File(filePath);
            filePath=file.getAbsolutePath();
            if(!file.exists()){
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            /*
            out=new BufferedOutputStream(new FileOutputStream(filePath));
            out.write(getGroupMsgByte(groupId, message));
            out.flush();
            out.close();
            */
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private static byte[] getGroupMsgByte(String groupId, Message message) {
//        String result=String.format("%s %s %s %s %d",groupId,message.getMsgId(),message.getTopic(),message.getProperty("filter"),message.getBornTime())+message.getBody().toString();
        String result=String.format("%s %s",groupId,message.getMsgId());
        byte[] bytes=result.getBytes();
        return bytes;
    }

    public static void saveMessage(Message message){
        String filePath;
        if(System.getProperty("userhome")!=null) {
            filePath = System.getProperty("userhome") + File.separator + "messageInfo/" + message.getMsgId();
        }else{
            filePath= "/userhome"+ File.separator  + "messageInfo/"+ message.getMsgId();
        }
        try {
            File file=new File(filePath);
            filePath=file.getAbsolutePath();
            if(!file.exists()){
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            out=new BufferedOutputStream(new FileOutputStream(filePath));
            String result=String.format("%s %d %s %s ",message.getMsgId(),message.getBornTime(),message.getTopic(),message.getProperty("filter"));
            byte[] body=message.getBody();
            out.write(result.getBytes());
            for(int i=0;i<body.length;i++){
                out.write(body[i]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static Map<String,Message> loadMessages(){
        String filePath;
        if(System.getProperty("userhome")!=null) {
            filePath = System.getProperty("userhome") + File.separator + "messageInfo/" ;
        }else{
            filePath= "/userhome"+ File.separator + "messageInfo/";
        }
        File directory=new File(filePath);
        File[] files=directory.listFiles();
        BufferedInputStream bi;
        Map<String,Message> map=new HashMap<String,Message>();
        for(File file:files){
            try {
//                System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());
                bi=new BufferedInputStream(new FileInputStream(file));
                byte[] bytes=new byte[bi.available()];
                bi.read(bytes);
                String temp=new String(bytes);
//                System.out.println("temp = " + temp);
                String[] strings=temp.split(" ");
                Message message=new Message();
                if(strings.length>=5) {
                    message.setMsgId(strings[0]);
                    message.setBornTime(Long.valueOf(strings[1]).longValue());
                    message.setTopic(strings[2]);
                    message.setProperty("filter", strings[3]);
                    StringBuffer body=new StringBuffer();
                    body.append(strings[4]);
                    for(int i=5;i<strings.length;i++){
                        body.append(" "+strings[i]);
                    }
                    message.setBody(body.toString().getBytes());
                    map.put(message.getMsgId(), message);
                }else{
                    System.out.println("invalid message in");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    /*
     这里返回groupId 和 msgId对应
     */
    public static Set<String> loadGroupMessage(){
        String filePath;
        if(System.getProperty("userhome")!=null) {
            filePath = System.getProperty("userhome") + File.separator + "store" + "/";
        }else{
            filePath= "/userhome"+ File.separator + "store" + "/";
        }
        File directory=new File(filePath);
        File[] files=directory.listFiles();
        Set<String> set=new HashSet();
        for(File file:files){
            set.add(file.getName());
        }
        return set;
    }

    public static void main(String[] args) {
        Message m=new Message();
        m.setBody("hello lalala".getBytes());
        m.setTopic("say");
        m.setProperty("filter", "area=uk");
        m.setMsgId("54646564");
        m.setBornTime(new Date().getTime());
//        DataHelper.saveMessage(m);
        long begin=System.currentTimeMillis();
//        for(int i=1000;i<20000;i++) {
//            m.setMsgId((i*1000)+"");
//            DataHelper.saveGroupMessage("user002", m);
//        }

        for(int i=1000;i<10000;i++){
            m.setMsgId((i*1000)+"");
            DataHelper.saveMessage(m);
        }
        long end=System.currentTimeMillis();
        System.out.println("(end-begin) = " + (end - begin));


        begin=System.currentTimeMillis();
                DataHelper.loadMessages();
//        DataHelper.loadGroupMessage();
        end=System.currentTimeMillis();
        System.out.println("(end-begin) = " + (end - begin));

    }
}
