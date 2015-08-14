package com.alibaba.middleware.race.mom.data;

import com.alibaba.middleware.race.mom.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by ivanmerlin on 2015/8/11.
 */
public class DataHelperV2 {


    static File file;
    static RandomAccessFile raf;
    static String filePath;
    final static String MESSAGELOG_PREFIX = "MessageLog";
    static int currentNo;

    static {
        if (System.getProperty("userhome") != null) {
            filePath = System.getProperty("userhome") + File.separator + "store/";
        } else {
            filePath = "/userhome" + File.separator + "store/";
        }
        File rootFile = new File(filePath);
        File[] files = rootFile.listFiles();
        if (files != null) {
            currentNo = files.length == 0 ? 0 : files.length - 1;
        }
        file = new File(filePath + MESSAGELOG_PREFIX + currentNo + ".store");

        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            raf = new RandomAccessFile(file, "rw");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String,Message> loadMessage(){
        Map<String,Message> map=new HashMap<String, Message>();
        if(raf!=null){

            try {
                raf.seek(0);
                String s=raf.readLine();
                while(s!=null){
                    String[] str=s.split(" ");
                    System.out.println("s=" + s);
                    Message m=new Message();
                    if(str.length>4) {
                        m.setProperty("status", str[0]);
                        m.setMsgId(str[1]);
                        m.setTopic(str[2]);
                        m.setBornTime(Long.valueOf(str[3]));
                        m.setProperty("condition", str[4]);
                    }
                    s=raf.readLine();
                    m.setBody(s.getBytes());
                    if(m.getMsgId()!=null&&m.getProperty("status").equals("not")){
                        map.put(m.getMsgId(),m);
                    }
                    s=raf.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("map.size() = " + map.size());
        return map;
    }
    public static boolean saveMessage(Message m) {
        if (raf == null) {
            return false;
        } else {
            try {
                raf.seek(raf.length());
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuilder builder = new StringBuilder("\r\nnot ");
            builder.append(m.getMsgId()).append(" ").append(m.getTopic()).append(" ");
            builder.append(m.getBornTime()).append(" ").append(m.getProperty("condition")).append("\r\n");

            /*
            这里可以通过改编码方式等 优化存储速度吧？
             */
            try {
                raf.writeBytes(builder.toString());
                raf.write(m.getBody());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

    }

    public static void main(String[] args) {
        System.out.println("DataHelperV2.main");
        Message m = new Message();
        m.setBody("hello lalaladfgd".getBytes());
        byte bytes[]=m.getBody();

        m.setTopic("say");
        m.setProperty("filter", "area=uk");
        Random random = new Random();
        m.setMsgId(Integer.toString(random.nextInt(1000000)));
        m.setBornTime(new Date().getTime());

        long begin = System.currentTimeMillis();
        DataHelperV2.saveMessage(m);
        DataHelperV2.loadMessage();

//        for (int i = 1000; i < 1002; i++) {
//            m.setMsgId((i * 1000) + "");
//            DataHelperV2.saveMessage(m);
//        }

        long end = System.currentTimeMillis();
        System.out.println("(end-begin) = " + (end - begin));

    }
}
