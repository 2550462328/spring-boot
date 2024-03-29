package cn.ictt.zhanghui.springboot_test.common.queue.redis;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.concurrent.*;

/**
 * redis队列消费者配置（绑定消费者）
 * @author ZhangHui
 * @date 2020/3/18
 * @return
 */
@Configuration
@ConditionalOnProperty(name = "spring.redis.enable", havingValue = "true", matchIfMissing = false)
public class RedisSubListenerConfig {
    //初始化监听器
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //监听seckill-channel频道
        container.addMessageListener(listenerAdapter, new PatternTopic("seckill-channel"));
        //这里__keyevent@0__:expired表示监听键过期事件，0是db0的意思
        container.addMessageListener(listenerAdapter, new PatternTopic("__keyevent@0__:expired"));
        /**
         * 如果不定义线程池，每一次消费都会创建一个线程，如果业务层面不做限制，就会导致秒杀超卖
         */
        ThreadFactory factory = new ThreadFactoryBuilder()
                .setNameFormat("redis-listener-pool-%d").build();
        Executor executor = new ThreadPoolExecutor(
                1,
                1,
                5L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10000),
                factory);
        container.setTaskExecutor(executor);
        container.setSubscriptionExecutor(Executors.newFixedThreadPool(1));
        return container;
    }

    //利用反射来创建监听到消息之后的执行方法
    @Bean
    MessageListenerAdapter listenerAdapter(RedisConsumer redisReceiver) {
        return new MessageListenerAdapter(redisReceiver, "receiveMessage");
    }

    //使用默认的工厂初始化redis操作模板
    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}