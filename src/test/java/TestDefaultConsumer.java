import com.alibaba.middleware.race.mom.ConsumeResult;
import com.alibaba.middleware.race.mom.DefaultConsumer;
import com.alibaba.middleware.race.mom.Message;
import com.alibaba.middleware.race.mom.MessageListener;

/**
 * Created by Administrator on 2015/8/10.
 */
public class TestDefaultConsumer {
    public static void main(String[] args){
        DefaultConsumer defaultConsumer = new DefaultConsumer();
        defaultConsumer.setGroupId("ggg111");
        defaultConsumer.subscribe("topic1", "3213", new MessageListener() {
            @Override
            public ConsumeResult onMessage(Message message) {
                return null;
            }
        });
        defaultConsumer.start();
//        defaultConsumer.stop();
    }
}
