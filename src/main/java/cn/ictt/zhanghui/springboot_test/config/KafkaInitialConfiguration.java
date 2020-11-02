package cn.ictt.zhanghui.springboot_test.config;

import org.apache.kafka.clients.admin.NewTopic;
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
public class KafkaInitialConfiguration {

    @Bean
    public NewTopic initalTopic(){
        return new NewTopic("topic.test",2,(short)2);
    }
}
