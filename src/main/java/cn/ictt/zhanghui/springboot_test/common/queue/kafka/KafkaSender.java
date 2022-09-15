package cn.ictt.zhanghui.springboot_test.common.queue.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * 消息生产者
 * @author Jarvis
 * @date 2018/8/3
 */
@Component
@Slf4j
@ConditionalOnProperty(name = "spring.kafka.enable", havingValue = "true", matchIfMissing = false)
public class KafkaSender<T> {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    /**
     * kafka 发送消息 默认发送分区0
     * @param obj 消息对象
     */
    public void send(String topicName, Object obj) {

        //发送消息
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, obj);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                //错误处理
            }
            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                //TODO 业务处理
            }
        });
    }

    /**
     * kafka 发送消息 指定分区和 key
     * @param obj 消息对象
     */
    public void sendWithPartition(String topicName, int partition, String key, Object obj) {

        //发送消息
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName,partition, key, obj);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                //错误处理
            }
            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                //TODO 业务处理
            }
        });
    }
}