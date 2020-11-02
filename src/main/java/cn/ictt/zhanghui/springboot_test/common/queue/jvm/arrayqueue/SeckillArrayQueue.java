package cn.ictt.zhanghui.springboot_test.common.queue.jvm.arrayqueue;

import cn.ictt.zhanghui.springboot_test.common.queue.disruptor.SeckillEvent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author ZhangHui
 * @version 1.0
 * @className SeckillArrayQueue
 * @description 这是描述信息
 * @date 2020/3/17
 */
public class SeckillArrayQueue {

    private static final int QUEUE_CAPABILITY = 100;
    private BlockingQueue<SeckillEvent> seckillQueue = new ArrayBlockingQueue<>(QUEUE_CAPABILITY);

    //默认构造方法私有化，防止外界实例化
    private SeckillArrayQueue(){};

    private static class SingletonHolder{
        //静态初始化器，由jvm保证线程安全
        private static SeckillArrayQueue seckillQueue = new SeckillArrayQueue();
    }

    public static SeckillArrayQueue getSeckillQueue(){
        return SingletonHolder.seckillQueue;
    }

    public boolean produce(SeckillEvent seckillEvent){
        /**
         * add(e) 队列未满返回true，已满抛出IllegalstateException("Queue full")异常
         * put(e) 队列未满返回true，已满进行阻塞，等待队列有空闲
         * offer(e) 队列未满返回true，已满直接返回false
         * offer(e, t, Timeunit) 队列未满返回true，已满等待t时间，如果还没有返回false
         */
        return seckillQueue.offer(seckillEvent);
    }

    public SeckillEvent consume() throws InterruptedException{
        /**
         * poll 移除首元素并返回，可以在规定时间内阻塞等待队列有符合元素，超时返回null
         * take 与poll不同的地方在于如果队列为空它会一直阻塞，直到有notempty.signal()调用才会返回
         */
        return seckillQueue.take();
    }
}
