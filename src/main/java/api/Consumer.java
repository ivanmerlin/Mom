package api;

import service.MessageListener;

/**
 * Created by ivan.wang on 2015/8/5.
 */

public interface Consumer {
    /**
     * ���������ߣ���ʼ���ײ���Դ��Ҫ���������úͶ��Ĳ�������֮��ִ��
     */
    void start();

    /**
     * �����Ĳ���
     *
     * @param topic
     *            ֻ���ܸ�topic����Ϣ
     * @param filter
     *            ���Թ������������� area=hz����ʾֻ����area����Ϊhz����Ϣ����Ϣ�Ĺ���Ҫ�ڷ���˽���
     * @param listener
     */
    void subscribe(String topic, String filter, MessageListener listener);

    /**
     * ������������id��brokerͨ�����id��ʶ�������߻���
     *
     * @param groupId
     */
    void setGroupId(String groupId);

    /**
     * ֹͣ�����ߣ�broker����Ͷ����Ϣ���������߻�����
     */
    void stop();
}
