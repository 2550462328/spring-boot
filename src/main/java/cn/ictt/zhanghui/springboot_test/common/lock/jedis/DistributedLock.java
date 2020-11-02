package cn.ictt.zhanghui.springboot_test.common.lock.jedis;

public interface DistributedLock {

    String acquire();

    boolean release(String token);

}
