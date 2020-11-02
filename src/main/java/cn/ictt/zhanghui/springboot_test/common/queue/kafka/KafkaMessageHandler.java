package cn.ictt.zhanghui.springboot_test.common.queue.kafka;

import cn.ictt.zhanghui.springboot_test.common.redis.RedisService;
import cn.ictt.zhanghui.springboot_test.common.socket.websocket.WebSocketServer;
import cn.ictt.zhanghui.springboot_test.exception.MyException;
import cn.ictt.zhanghui.springboot_test.service.SeckillService;
import cn.ictt.zhanghui.springboot_test.util.cipher.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class KafkaMessageHandler {

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private RedisService redisService;

    /**
     * 监听zhanghui.test 的 topic
     * @param record
     * @param topic  topic
     */
    @KafkaListener(id = "tut", topics = "topic.test" )
    public void listen(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        //判断是否NULL
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {
            //获取消息
            Object message = kafkaMessage.get();

            log.info("Receive： +++++++++++++++ Topic:" + topic+"\n");
            log.info("Receive： +++++++++++++++ Record:" + record+"\n");
            log.info("Receive： +++++++++++++++ Message:" + message+"\n");
        }
    }

    @KafkaListener(id = "seckillTopic", topics = "topic.seckill")
    public void listen_seckill(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        //判断是否NULL
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {
            //获取消息
            String message = kafkaMessage.get().toString();
            if(!StringUtil.isNumber(message)){
                throw new MyException(message + "无效的商品编号！");
            }
            int goodsId = Integer.valueOf(message);
            boolean isSuccess = seckillService.doSeckill_pem(goodsId);
            if(isSuccess){
                WebSocketServer.sendInfo("秒杀成功","1");
            }else{
                WebSocketServer.sendInfo("秒杀失败","1");
                redisService.cacheValue(message,"finished");
            }
        }
    }
}
