package test;

import com.alibaba.middleware.race.mom.*;

/**
 * Created by ivan.wang on 2015/8/5.
 */
public class TestThread extends Thread{
    MessageListener listener;
    int id;
    public TestThread(int id){
        listener= new MessageListener() {
            public ConsumeResult onMessage(Message message) {
                System.out.println("get on message");
                return null;
            }
        };
        id=id;
    }
    public void run() {
        Consumer consumer=new DefaultConsumer();
        consumer.start();
        consumer.setGroupId("group-" + id);
        System.out.println("ready to subscribe");
        consumer.subscribe("helloTopic", "", listener);
        System.out.println("finish subscribe");

    }

    @Override
    public synchronized void start() {
        super.start();
    }
}