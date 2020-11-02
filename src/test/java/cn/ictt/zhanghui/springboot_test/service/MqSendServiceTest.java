package cn.ictt.zhanghui.springboot_test.service;

import cn.ictt.zhanghui.springboot_test.common.queue.mq.MqMessageSend;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class MqSendServiceTest {
    @Autowired
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
