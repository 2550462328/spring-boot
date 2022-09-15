package cn.ictt.zhanghui.springboot_test.base.config.threadpool;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池相关配置
 *
 * @author huizhang43
 */
@Configuration
public class ThreadPoolTaskExecutorConfig {
    
    @Qualifier("yysTaskExecutor")
    @Bean("yysTaskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor freezeExecutor = new VisiableThreadPoolTaskExecutor();
        freezeExecutor.setCorePoolSize(20);
        freezeExecutor.setKeepAliveSeconds(30000);
        freezeExecutor.setMaxPoolSize(100);
        freezeExecutor.setQueueCapacity(200);
        freezeExecutor.setThreadNamePrefix("yys-user-pool-");
        freezeExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return freezeExecutor;
    }
}
