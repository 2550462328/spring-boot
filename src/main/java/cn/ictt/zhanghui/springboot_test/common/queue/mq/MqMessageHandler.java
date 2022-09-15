package cn.ictt.zhanghui.springboot_test.common.queue.mq;

import cn.ictt.zhanghui.springboot_test.base.exception.enums.BusinessResponseEnum;
import cn.ictt.zhanghui.springboot_test.common.redis.RedisService;
import cn.ictt.zhanghui.springboot_test.common.socket.websocket.WebSocketServer;
import cn.ictt.zhanghui.springboot_test.business.service.SeckillService;
import cn.ictt.zhanghui.springboot_test.base.util.cipher.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
@ConditionalOnProperty(name = "spring.activemq.enable", havingValue = "true", matchIfMissing = false)
public class MqMessageHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Autowired(required = false)
    private RedisService redisService;

    @JmsListener(destination = "queue-2", containerFactory = "queueListenerContainer")
    public void recieveQ1(String message) {
        logger.info("接收Q1消息：" + message + "###################");
    }

    @JmsListener(destination = "queue-2", containerFactory = "queueListenerContainer")
    public void recieveQ2(String message) {
        logger.info("接收Q2消息：" + message + "###################");
    }

    @JmsListener(destination = "topic-2", containerFactory = "topicListenerContainer")
    public void recieveT1(String message) {
        logger.info("接收T1消息：" + message + "###################");
    }

    @JmsListener(destination = "seckill-queue", containerFactory = "queueListenerContainer")
    public void doSeckillTask(String message) {
        if(StringUtils.isEmpty(message) || !StringUtil.isNumber(message)){
            BusinessResponseEnum.INVAID_GOOD_NUMBER.assertFail();
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
