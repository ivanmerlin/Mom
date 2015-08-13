package com.alibaba.middleware.race.mom.data;

import com.alibaba.middleware.race.mom.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by ivan.wang on 2015/8/13.
 */
public class SyncDataHelper {
    static RandomAccessFile raf ;
    static FileChannel channel;
    static MappedByteBuffer out;
    final static String MESSAGELOG_PREFIX="MessageLog";
    static String filePath;
    static int currentNo;
    static int saveTimes;

    static {
        saveTimes=0;
        if(System.getProperty("userhome")!=null){
            filePath=System.getProperty("userhome")+File.separator+"store/";
        }else{
            filePath="/userhome"+File.separator+"store/";
        }
        File rootFile=new File(filePath);
        File[] files=rootFile.listFiles();
        currentNo=files.length;

        File file=new File(filePath+MESSAGELOG_PREFIX+currentNo);

        /*
        判断当前读到第几个文件了
         */
        if(!file.exists()){
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
        }
        if(file.length()>1024*1024){
            currentNo++;
            file=new File(filePath+MESSAGELOG_PREFIX+currentNo);
        }
        try {
            raf=new RandomAccessFile(file,"rw");
            channel =raf.getChannel();
            out = channel.map(FileChannel.MapMode.READ_WRITE,0, channel.size());
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
    public boolean saveMessage(Message message){
        if(out==null){
            return false;
        }else{
            StringBuilder builder=new StringBuilder().append("not ");
            builder.append(message.getMsgId()).append(" ").append(message.getTopic()).append(" ");
            builder.append(message.getBornTime()).append(" ").append(message.getProperty("condition")).append(" ");
            builder.append(message.getBody());
            /*
            这里可以通过改编码方式等 优化存储速度吧？
             */
            byte[] bytes= builder.toString().getBytes();
            out.put(bytes);
            out.force();
            return true;
        }
    }

}
