package cn.ictt.zhanghui.springboot_test;

import cn.ictt.zhanghui.springboot_test.enums.Events;
import cn.ictt.zhanghui.springboot_test.enums.States;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.statemachine.StateMachine;

@SpringBootApplication
//@EnableScheduling
@EnableAsync
@EnableCaching
@EnableWebSecurity
//@MapperScan("cn.ictt.zhanghui.springboot_test.pojo.mapper")
public class SpringbootTestApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootTestApplication.class, args);
    }

    @Bean
    public ObjectMapper serializingObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Autowired
    private StateMachine<States, Events> stateMachine;

    @Override
    public void run(String... args) throws Exception {
        stateMachine.start();
        stateMachine.sendEvent(Events.PAY);
        stateMachine.sendEvent(Events.RECEIVE);
    }
}
