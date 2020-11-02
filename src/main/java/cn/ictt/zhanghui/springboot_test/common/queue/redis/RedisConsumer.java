package cn.ictt.zhanghui.springboot_test.common.queue.redis;

import cn.ictt.zhanghui.springboot_test.common.redis.RedisService;
import cn.ictt.zhanghui.springboot_test.common.socket.websocket.WebSocketServer;
import cn.ictt.zhanghui.springboot_test.service.SeckillService;
import cn.ictt.zhanghui.springboot_test.util.cipher.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * redis队列的消费者（监听事件）
 */
@Service
public class RedisConsumer {

	@Autowired
	private SeckillService seckillService;
	@Autowired
	private RedisService redisService;

	private Logger logger = LoggerFactory.getLogger(RedisConsumer.class);
    public void receiveMessage(String message) {
		if(!StringUtil.isNumber(message)){
			// 监听redis键过期事件
			// logger.info("{}键已失效，请尽快处理",message);
			if(message.endsWith("amount")){
				//红包过期
				logger.info("红包{}已过期，执行退款",message);
				/*
				* 红包退款
				*/
			}
			return;
		}
		//监听秒杀事件
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