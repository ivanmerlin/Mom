package com.alibaba.middleware.race.mom.broker;

import com.alibaba.middleware.race.mom.broker.thread.SendThread;

/**
 * Created by ivan.wang on 2015/8/6.
 */
public class DispatchThread extends Thread {

    static  final int MAX_CAPACITY=8000;
    static  int current=0;

    @Override
    public void run() {
        while(true){
            if(current>MAX_CAPACITY){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(MessageCenter.queue.size()==0){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            MessageThread headThread=new MessageThread(MessageCenter.queue.poll());
            headThread.start();


        }
    }

    public synchronized int getCurrent(){
        return current;
    }
    public synchronized void addCurrent(int i){
        current+=i;
    }
    public synchronized void reduceCurrent(int i){
        current-=i;
    }
    public synchronized int getAndIncrement(int i){
        int result=getCurrent();
        addCurrent(i);
        return result;
    }
}
