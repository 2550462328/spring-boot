package cn.ictt.zhanghui.springboot_test.base.config.mq;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName KafkaInitialConfiguration
 * @Description: kafka的Topic初始化，指定分区数和副本数
 * @Author: ZhangHui
 * @Date: 2020/9/23
 * @Version：1.0
 */
@Configuration
@ConditionalOnProperty(name = "spring.kafka.enable", havingValue = "true", matchIfMissing = false)
public class KafkaInitialConfiguration {

    @Bean
    public NewTopic initalTopic(){
        return new NewTopic("topic.test",2,(short)2);
    }
}
