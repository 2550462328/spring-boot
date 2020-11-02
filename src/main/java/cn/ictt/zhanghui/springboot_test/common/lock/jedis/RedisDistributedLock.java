package cn.ictt.zhanghui.springboot_test.common.lock.jedis;

import cn.ictt.zhanghui.springboot_test.util.cipher.StringUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.UUID;

/**
 * @author ZhangHui
 * @version 1.0
 * @className RedisDistributedLocl
 * @description 使用jedis实现redis的分布式锁
 * 加锁操作的正确姿势为：
 * 1. 使用setnx命令保证互斥性
 * 2. 需要设置锁的过期时间，避免死锁
 * 3. setnx和设置过期时间需要保持原子性，避免在设置setnx成功之后在设置过期时间客户端崩溃导致死锁
 * 4. 加锁的Value 值为一个唯一标示。可以采用UUID作为唯一标示。加锁成功后需要把唯一标示返回给客户端来用来客户端进行解锁操作
 * <p>
 * 解锁的正确姿势为：
 * 　　1. 需要拿加锁成功的唯一标示要进行解锁，从而保证加锁和解锁的是同一个客户端。
 * 　　2. 解锁操作需要比较唯一标示是否相等，相等再执行删除操作。这2个操作可以采用Lua脚本方式使2个命令的原子性。
 *
 * 这种方式仅适用于单机版redis的情况下！！！
 * @date 2020/8/20
 */
@Slf4j
public class RedisDistributedLock implements DistributedLock {

    private static final String LOCK_SUCCESS = "OK";
    private static final Long RELEASE_SUCCESS = 1L;
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "EX";

    private Jedis jedis;

    private String lockKey;

    int expireTime = 10;

    int acquireTimeout = 1 * 1000;

    public RedisDistributedLock(Jedis jedis, String lockKey) {
        this.jedis = jedis;
        this.lockKey = lockKey;
    }

    public RedisDistributedLock(Jedis jedis, String lockKey, int expireTime) {
        this.jedis = jedis;
        this.lockKey = lockKey;
        this.expireTime = expireTime;
    }

    public RedisDistributedLock(Jedis jedis, String lockKey, int expireTime, int acquireTimeout) {
        this.jedis = jedis;
        this.lockKey = lockKey;
        this.expireTime = expireTime;
        this.acquireTimeout = acquireTimeout;
    }

    @Override
    public String acquire() {
        // 获取锁的超时时间，超出这个时间放弃获取锁
        long end = System.currentTimeMillis() + acquireTimeout;

        // 随机生成一个唯一标识，用于确定加锁和解锁的是同一客户端
        String requireToken = UUID.randomUUID().toString();

        while (System.currentTimeMillis() < end) {
            String result = jedis.set(lockKey, requireToken, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
            if (LOCK_SUCCESS.equals(result)) {
                return requireToken;
            }

//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                log.error("acquire lock due to error ", e);
//            }
        }
        return null;
    }

    @Override
    public boolean release(String token) {
        if (StringUtil.isBlank(token)) {
            return false;
        }

        // 使用Lua脚本保证检查和删除操作的原子性
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

        Object result = new Object();

        try {
            result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(token));

            if (RELEASE_SUCCESS.equals(result)) {
                log.info("release lock success,requireToken:{}", token);
                return true;
            }
        } catch (Exception e) {
            log.error("release lock due to error ", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        log.info("release lock failed, requestToken:{},result:{}", token, result);

        return false;
    }
}
