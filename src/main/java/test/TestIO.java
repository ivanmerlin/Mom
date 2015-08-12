package test;

import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by ivan.wang on 2015/8/12.
 */
public class TestIO {

public static void test1(){
    File file;
    BufferedWriter bw = null;
    int times=1000;
    long begin=System.currentTimeMillis();
    for(int i=0;i<times;i++){
        file=new File("D:/testIOOOOO/"+i);
        if(!file.exists()){
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
        }
        try {
            bw=new BufferedWriter(new FileWriter(file));
            bw.write(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bw!=null){
            try {
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    long end=System.currentTimeMillis();
    System.out.println("(end-begin = " + (end-begin));

    file=new File("D:/testIOOOOO/log2");
    if(!file.exists()){
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    BufferedWriter bw2 = null;
    begin=System.currentTimeMillis();

    try {
        bw2=new BufferedWriter(new FileWriter(file,true));
        for(int i=0;i<times;i++){
            bw2.write(i);
        }
        bw2.flush();
        bw2.close();
    } catch (IOException e) {
        e.printStackTrace();
    }

    end=System.currentTimeMillis();
    System.out.println("(end-begin = " + (end-begin));

}

    public static void test2(){
        String fileName="D:/testIOOOOO/log1";
        File file;
        BufferedWriter bw = null;
        int times=1000;
        long begin=System.currentTimeMillis();
        for(int i=0;i<times;i++){
            file=new File("D:/testIOOOOO/"+i);
            if(!file.exists()){
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
            }
            try {
                bw=new BufferedWriter(new FileWriter(file));
                bw.write(i);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(bw!=null){
                try {
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        long end=System.currentTimeMillis();
        System.out.println("(end-begin = " + (end-begin));

        file=new File("D:/testIOOOOO/log2");
        if(!file.exists()){
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        begin=System.currentTimeMillis();
        for(int i=0;i<times;i++){
            file=new File("D:/testIOOOOO/"+i);
            if(!file.exists()){
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
            }
            try {
                FileUtils.write(file, Integer.toString(i), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        end=System.currentTimeMillis();
        System.out.println("(end-begin = " + (end-begin));
    }
    public static void test3(){
        File file;
        int times=10000;
        file=new File("D:/testIOOOOO/log3.txt");
        if(!file.exists()){
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long begin=System.currentTimeMillis();
        for(int i=0;i<times;i++){
            try {
                FileUtils.write(file,Integer.toString(i),true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long end=System.currentTimeMillis();
        System.out.println("FileUtils end-begin = " + (end-begin));

        BufferedWriter bw2 = null;
        file=new File("D:/testIOOOOO/log4.txt");
        if(!file.exists()){
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        begin=System.currentTimeMillis();
        try {
            bw2=new BufferedWriter(new FileWriter(file,true));
            for(int i=0;i<times;i++){
                bw2.write(Integer.toString(i));
            }
            bw2.flush();
            bw2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        end=System.currentTimeMillis();
        System.out.println("BufferedWriter end-begin = " + (end-begin));







    }


    public static void main(String[] args) {
//        TestIO.test2();
        TestIO.test3();
    }
}
