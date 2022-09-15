package cn.ictt.zhanghui.springboot_test.common.queue.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * redis队列的生产者(redis的发布订阅)
 */
@Service
@ConditionalOnProperty(name = "spring.redis.enable", havingValue = "true", matchIfMissing = false)
public class RedisSender {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    //向通道发送消息的方法
    public void sendChannelMess(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }
}
