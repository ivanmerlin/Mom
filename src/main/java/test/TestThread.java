package test;

import com.alibaba.middleware.race.mom.DefaultConsumer;
import com.alibaba.middleware.race.mom.bean.ConsumeResult;
import com.alibaba.middleware.race.mom.bean.Message;
import com.alibaba.middleware.race.mom.service.MessageListener;

/**
 * Created by ivan.wang on 2015/8/5.
 */
public class TestThread implements Runnable{
    MessageListener listener;
    public TestThread(){
        listener= new MessageListener() {
            public ConsumeResult onMessage(Message message) {
                System.out.println("get on message");
                return null;
            }
        };
    }
    public void run() {
        DefaultConsumer consumer=new DefaultConsumer();
        consumer.start();
        consumer.subscribe("helloTopic","",listener);
    }
}