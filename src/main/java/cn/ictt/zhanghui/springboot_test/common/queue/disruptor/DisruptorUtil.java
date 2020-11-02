package cn.ictt.zhanghui.springboot_test.common.queue.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.ThreadFactory;

/**
 * @author ZhangHui
 * @version 1.0
 * @className DisruptorUtil
 * @description Disruptor队列构造
 * @date 2020/3/17
 */
public class DisruptorUtil {

    static Disruptor<SeckillEvent> disruptor = null;

    static{
        SeckillEventFactory factory = new SeckillEventFactory();
        int ringBufferSize = 1024;
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        };
        disruptor = new Disruptor<SeckillEvent>(factory,ringBufferSize,threadFactory);
        disruptor.handleEventsWith(new SeckillEventConsumer());
        disruptor.start();
    }

    /**
     * 生产SeckillEvent
     * @date 2020/3/17
     * @param seckillEvent
     * @return void
     */
    public static void produce(SeckillEvent seckillEvent){
        RingBuffer<SeckillEvent> ringBuffer = disruptor.getRingBuffer();
        SeckillEventProducer producer = new SeckillEventProducer(ringBuffer);
        producer.produceEvent(seckillEvent.getSeckill_id());
    }
}
