package cn.ictt.zhanghui.springboot_test.common.queue.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * mq消息生产
 * @author ZhangHui
 * @date 2020/3/17
 * @param null
 * @return
 */
@Component
public class MqMessageSend {
    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsMessagingTemplate jmsQueueTemplate;

    @Autowired
    @Qualifier("jmsTopicTemplate")
    private JmsMessagingTemplate jmsTopicTemplate;

    /**
     * 功能描述
     * @author ZhangHui
     * @date 2019/5/28
     * @param queueName
     * @param message
     * @return void
     */
    public void sendQueue(String queueName, String message) {
        //发送queue消息
        jmsQueueTemplate.convertAndSend(queueName, message);
    }

    /**
     * 功能描述
     * @author ZhangHui
     * @date 2019/5/28
     * @param topicName
     * @param message
     * @return void
     */
    public void sendTopic(String topicName, String message) {
        //发送queue消息
        jmsTopicTemplate.convertAndSend(topicName, message);
    }
}
