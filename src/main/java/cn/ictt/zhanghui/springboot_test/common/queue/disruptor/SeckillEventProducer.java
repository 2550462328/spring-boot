package cn.ictt.zhanghui.springboot_test.common.queue.disruptor;

import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.RingBuffer;

/**
 * @author ZhangHui
 * @version 1.0
 * @className SeckillEventProducer
 * @description 秒杀事件生产者
 * @date 2020/3/17
 */
public class SeckillEventProducer {
    private final static EventTranslatorVararg<SeckillEvent> translator = new EventTranslatorVararg<SeckillEvent>() {
        @Override
        public void translateTo(SeckillEvent seckillEvent, long seq, Object... objects) {
            seckillEvent.setSeckill_id((Integer) objects[0]);
        }
    };

    private RingBuffer<SeckillEvent> ringBuffer;

    public SeckillEventProducer(RingBuffer<SeckillEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * 向disruptor队列中放入秒杀事件
     * @author ZhangHui
     * @date 2020/3/17
     * @param seckillId
     * @return void
     */
    public void produceEvent(int seckillId){
        this.ringBuffer.publishEvent(translator,seckillId);
    }
}
