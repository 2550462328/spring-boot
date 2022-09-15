package cn.ictt.zhanghui.springboot_test.base;

import cn.ictt.zhanghui.springboot_test.SpringbootTestApplicationTests;
import cn.ictt.zhanghui.springboot_test.business.pojo.domain.UserOperate;
import cn.ictt.zhanghui.springboot_test.common.queue.kafka.KafkaSender;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 测试kafka发送数据
 * @author ZhangHui
 * @date 2020/9/23
 */
public class KafkaSenderTest extends SpringbootTestApplicationTests {
    @Autowired(required = false)
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
