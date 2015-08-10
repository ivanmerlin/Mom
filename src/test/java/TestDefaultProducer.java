import com.alibaba.middleware.race.mom.DefaultProducer;
import com.alibaba.middleware.race.mom.Message;
import com.alibaba.middleware.race.mom.SendCallback;
import com.alibaba.middleware.race.mom.SendResult;



/**
 * Created by Administrator on 2015/8/10.
 */
public class TestDefaultProducer {
    static DefaultProducer defaultProducer;

    public static void main(String[] args){
        defaultProducer = new DefaultProducer();
        defaultProducer.start();

        Message message = new Message();
        message.setMsgId("12345");
        byte[] body = "adsadasf".getBytes();
        message.setBody(body);
        message.setTopic("topic1");


        /**同步调用测试*/
        SendResult sendResult = defaultProducer.sendMessage(message);



        /**异步调用测试*/
        defaultProducer.asyncSendMessage(message, new SendCallback() {
            @Override
            public void onResult(SendResult sendResult) {
                System.out.println("call back success!!");
            }
        });

        defaultProducer.stop();
    }
}
