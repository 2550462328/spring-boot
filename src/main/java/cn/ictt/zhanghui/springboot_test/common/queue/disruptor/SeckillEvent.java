package cn.ictt.zhanghui.springboot_test.common.queue.disruptor;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ZhangHui
 * @version 1.0
 * @className SeckillEvent
 * @description 一个秒杀事件
 * @date 2020/3/17
 */
@Getter
@Setter
public class SeckillEvent {
    private int seckill_id;
}
