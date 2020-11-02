package cn.ictt.zhanghui.springboot_test.service;

import cn.ictt.zhanghui.springboot_test.common.queue.kafka.KafkaSender;
import cn.ictt.zhanghui.springboot_test.domain.UserOperate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试kafka发送数据
 * @author ZhangHui
 * @date 2020/9/23
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaSenderTest {
    @Autowired
    private KafkaSender<UserOperate> sender;
    @Test
    public void hello() throws Exception {
        for(int i = 0; i < 100; i++){
            UserOperate user_operate = new UserOperate("admin"+i, "admin"+i);
            sender.sendWithPartition("topic.test",1,"zhang",user_operate.toString());
            sender.sendWithPartition("topic.test",0,"zhang",user_operate.toString());
        }
    }
}
