package cn.ictt.zhanghui.springboot_test.common.queue.jvm.arrayqueue;

import cn.ictt.zhanghui.springboot_test.common.queue.disruptor.SeckillEvent;
import cn.ictt.zhanghui.springboot_test.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author ZhangHui
 * @version 1.0
 * @className SeckillQueueHandler
 * @description 普通阻塞Queue中事件的处理
 * @date 2020/3/17
 */
@Component
public class SeckillQueueHandler implements ApplicationRunner {

    Logger logger = LoggerFactory.getLogger(SeckillQueueHandler.class);

    @Autowired
    private SeckillService seckillService;

    @Override
    public void run(ApplicationArguments args) {
        logger.info("seckillArrayQueue消费线程已启动！");
        new Thread(() -> {
            while (true) {
                SeckillEvent seckillEvent = null;
                try {
                    seckillEvent = SeckillArrayQueue.getSeckillQueue().consume();
                } catch (InterruptedException e) {
                    logger.error("消费seckillArrayQueue中秒杀事件异常：" + e.getMessage(), e);
                }
                if (seckillEvent != null) {
                    seckillService.doSeckill(seckillEvent.getSeckill_id());
                }
            }
        }, "AQConsumeThread").start();
    }
}
