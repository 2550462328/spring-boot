package cn.ictt.zhanghui.springboot_test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数10
        executor.setCorePoolSize(10);
        //最大线程数20
        executor.setMaxPoolSize(20);
        //用来缓冲执行任务的队列容量
        executor.setQueueCapacity(200);
        //运行线程的空闲时间
        executor.setKeepAliveSeconds(60);
        //线程池名的前缀
        executor.setThreadNamePrefix("taskExecutor-");
        //对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //设置线程池关闭的时候要等待所有任务完成后再继续销毁其他bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //设置等待所有任务完成是的最大等待时间，超过将强行销毁
        executor.setAwaitTerminationSeconds(60);
        return executor;
    }
}
