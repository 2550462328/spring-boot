package cn.ictt.zhanghui.springboot_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@EnableScheduling
//@EnableAsync
//@EnableCaching
//@MapperScan("cn.ictt.zhanghui.springboot_test.pojo.mapper")
@EntityScan("cn.ictt.zhanghui.springboot_test.business.pojo.domain")
public class SpringbootTestApplication implements CommandLineRunner {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringbootTestApplication.class, args);
    }

    @Bean
    public ObjectMapper serializingObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

//    @Autowired
//    private StateMachine<States, Events> stateMachine;

    @Override
    public void run(String... args) throws Exception {
//        stateMachine.start();
//        stateMachine.sendEvent(Events.PAY);
//        stateMachine.sendEvent(Events.RECEIVE);
    }
}
