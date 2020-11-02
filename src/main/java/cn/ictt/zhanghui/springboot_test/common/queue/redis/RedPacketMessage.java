package cn.ictt.zhanghui.springboot_test.common.queue.redis;

import java.io.Serializable;

/**
 * 红包队列消息
 */
public class RedPacketMessage implements Serializable {

    /**
     * 红包 ID
     */
    private  long redPacketId;

    /**
     * 创建时间戳
     */
    private  long timestamp;

    public RedPacketMessage() {

    }

    public RedPacketMessage(long redPacketId) {
        this.redPacketId = redPacketId;
        this.timestamp = System.currentTimeMillis();
    }

    public long getRedPacketId() {
        return redPacketId;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
