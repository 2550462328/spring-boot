package cn.ictt.zhanghui.springboot_test.base;

import cn.ictt.zhanghui.springboot_test.SpringbootTestApplicationTests;
import cn.ictt.zhanghui.springboot_test.common.queue.mq.MqMessageSend;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MqSendServiceTest extends SpringbootTestApplicationTests {
    @Autowired(required = false)
    private MqMessageSend sender;

    @Test
    public void sendQueueTest() throws Exception {
        sender.sendQueue("queue-2","hello-world");
    }
    @Test
    public void sendTopicTest() throws Exception {
        sender.sendTopic("topic-2","hello-world");
    }
}
