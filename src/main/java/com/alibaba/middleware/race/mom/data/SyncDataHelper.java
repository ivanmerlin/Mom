package com.alibaba.middleware.race.mom.data;

import com.alibaba.middleware.race.mom.Message;
import com.alibaba.middleware.race.mom.utils.StringUtils;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by ivan.wang on 2015/8/13.
 */
public class SyncDataHelper {
    static RandomAccessFile raf;
    static FileChannel channel;
    static MappedByteBuffer out;
    final static String MESSAGELOG_PREFIX = "MessageLog";
    static String filePath;
    static int currentNo;
    static int saveTimes;
    static File file;
    final static int FILE_SIZE = 1024 * 1024 * 2;

    static {
        saveTimes = 0;
        if (System.getProperty("userhome") != null) {
            filePath = System.getProperty("userhome") + File.separator + "store/";
        } else {
            filePath = "/userhome" + File.separator + "store/";
        }
        File rootFile = new File(filePath);
        File[] files = rootFile.listFiles();
        if (files != null) {
            currentNo = files.length==0?0:files.length-1;
        }
        file = new File(filePath + MESSAGELOG_PREFIX + currentNo + ".store");
//        loadMessage();
        /*
        判断当前读到第几个文件了
         */
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
        }

        System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());
        try {
            raf = new RandomAccessFile(file, "rw");
            channel = raf.getChannel();
            out = channel.map(FileChannel.MapMode.READ_WRITE, 0, FILE_SIZE);
            out.load();
//            checkSize();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void loadMessage() {
        try {
            BufferedReader br=new BufferedReader(new FileReader(file));
            String s=br.readLine();
            while(s!=null&& StringUtils.isNotBlank(s)){
                System.out.print(s);
                s=br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    还是不确定是不是要存groupId
    这里先保存单纯的message信息
     */
    public static boolean saveMessage(Message message) {
        checkSize();
        if (out == null) {
            return false;
        } else {
            StringBuilder builder = new StringBuilder().append("not ");
            builder.append(message.getMsgId()).append(" ").append(message.getTopic()).append(" ");
            builder.append(message.getBornTime()).append(" ").append(message.getProperty("condition")).append(" ");
            builder.append(message.getBody());
            /*
            这里可以通过改编码方式等 优化存储速度吧？
             */
            byte[] bytes = builder.toString().getBytes();
            out.put(bytes);
            out.putChar('\r');
            out.putChar('\n');
            out.force();
            System.out.println("out.position() = " + out.position());

            return true;
        }
    }

    public static void checkSize() {
        if (out.position() > FILE_SIZE - 200) {
            file = new File(filePath + MESSAGELOG_PREFIX + currentNo + ".store");
            try {
                raf = new RandomAccessFile(file, "rw");
                channel = raf.getChannel();
                out = channel.map(FileChannel.MapMode.READ_WRITE, 0, FILE_SIZE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Message m = new Message();
        m.setBody("hello lalaladfgd".getBytes());
        m.setTopic("say");
        m.setProperty("filter", "area=uk");
        Random random = new Random();
        m.setMsgId(Integer.toString(random.nextInt(1000000)));
        m.setBornTime(new Date().getTime());
        long begin = System.currentTimeMillis();
//        SyncDataHelper.saveMessage(m);
//        SyncDataHelper.saveMessage(m);
//        for (int i = 1000; i < 2000; i++) {
//            m.setMsgId((i * 1000) + "");
//            SyncDataHelper.saveMessage(m);
//        }

        SyncDataHelper.loadMessage();
        long end = System.currentTimeMillis();
//        System.out.println("(end-begin) = " + (end - begin));
//        try {
//            SyncDataHelper.raf.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



    }

}
