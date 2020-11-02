package cn.ictt.zhanghui.springboot_test.common.queue.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * @author ZhangHui
 * @version 1.0
 * @className SeckillEventFactory
 * @description 这是描述信息
 * @date 2020/3/17
 */
public class SeckillEventFactory implements EventFactory<SeckillEvent> {
    @Override
    public SeckillEvent newInstance() {
        return new SeckillEvent();
    }
}
