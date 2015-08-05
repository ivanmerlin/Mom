package api;

import bean.Message;
import service.SendCallback;

/**
 * Created by ivan.wang on 2015/8/5.
 */

public interface Producer {
    /**
     * ���������ߣ���ʼ���ײ���Դ������������������Ϻ󣬲��ܵ����������
     */
    void start();
    /**
     * ���������߿ɷ��͵�topic
     * @param topic
     */
    void setTopic(String topic);
    /**
     * ����������id��brokerͨ�����id��ʶ�������߼�Ⱥ
     * @param groupId
     */
    void setGroupId(String groupId);
    /**
     * ������Ϣ
     * @param message
     * @return
     */
    SendResult sendMessage(Message message);
    /**
     * �첽callback������Ϣ����ǰ�̲߳�������broker����ack�󣬴���callback
     * @param message
     * @param callback
     */
    void asyncSendMessage(Message message, SendCallback callback);
    /**
     * ֹͣ�����ߣ�������Դ
     */
    void stop();

}
