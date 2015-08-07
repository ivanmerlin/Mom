import com.alibaba.middleware.race.mom.Message;

/**
 * Created by ivan.wang on 2015/8/6.
 */
public class TestJson {







    public static void main(String[] args) {
        Message message=new Message();
        message.setTopic("hi-topic");
        message.setBody("hilloosdfsd".getBytes());
        message.setProperty("filter","area=hk");
        long startTime=System.currentTimeMillis();   //获取开始时间
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("使用jackson程序运行时间： "+(endTime-startTime)+"ms");
        startTime=System.currentTimeMillis();   //获取开始时间

         endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("使用json程序运行时间： "+(endTime-startTime)+"ms");

    }
}
