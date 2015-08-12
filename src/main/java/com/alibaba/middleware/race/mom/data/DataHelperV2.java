package com.alibaba.middleware.race.mom.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

/**
 * Created by ivanmerlin on 2015/8/11.
 */
public class DataHelperV2 {



    RandomAccessFile randomFile;
    private static int count = 57600;
    static DataHelperV2 instance;
    private DataHelperV2(){
        String path;
        if(System.getProperty("userhome")!=null){
            path=System.getProperty("userhome");
        }else{
            path="/userhome";
        }
        path=path+ File.separator+"store/log";
        File file=new File(path);
        System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());
        if(!file.exists()){
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
        }
        try {
            randomFile=new RandomAccessFile(path,"rw");
            MappedByteBuffer out = randomFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, count);


            for(int i=0;i<10;i++){
                out.put("q".getBytes());
            }
            for (int i = 0; i < 10; i++) {
                System.out.print((char) out.get(i));
            }
            System.out.println("Reading from Memory Mapped File is completed");
            randomFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static DataHelperV2 getInstance(){
        if(instance==null){
            instance=new DataHelperV2();
        }
        return instance;
    }

    public static void main(String[] args) {

        DataHelperV2.getInstance();


    }
}
