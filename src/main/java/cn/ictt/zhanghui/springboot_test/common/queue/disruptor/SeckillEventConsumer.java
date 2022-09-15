package cn.ictt.zhanghui.springboot_test.common.queue.disruptor;

import cn.ictt.zhanghui.springboot_test.business.service.SeckillService;
import cn.ictt.zhanghui.springboot_test.base.util.frame.SpringUtils;
import com.lmax.disruptor.EventHandler;

/**
 * @author ZhangHui
 * @version 1.0
 * @className SeckillEventConsumer
 * @description 秒杀事件消费者监听器
 * @date 2020/3/17
 */
public class SeckillEventConsumer implements EventHandler<SeckillEvent> {

    private SeckillService seckillService = (SeckillService)SpringUtils.getBean("seckillService");

    @Override
    public void onEvent(SeckillEvent seckillEvent, long seq, boolean bool) throws Exception {
        seckillService.doSeckill(seckillEvent.getSeckill_id());
    }
}
