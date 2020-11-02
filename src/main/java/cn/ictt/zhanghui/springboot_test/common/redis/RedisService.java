package cn.ictt.zhanghui.springboot_test.common.redis;

import cn.ictt.zhanghui.springboot_test.common.redis.filter.BloomFilterHelper;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhangHui
 * @version 1.0
 * @className RedisService
 * @description redis的一些api的封装
 * @date 2020/3/18
 */
@Component("redisService")
public class RedisService {
    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private RedisTemplate<Serializable, Serializable> redisTemplate;

    /**
     * 前缀
     */
    public static final String KEY_PREFIX_VALUE = "zh:seckill:value:";

    /**
     * 缓存value操作
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheValue(String k, Serializable v, long time) {
        String key = KEY_PREFIX_VALUE + k;
        try {
            ValueOperations<Serializable, Serializable> valueOps = redisTemplate.opsForValue();
            valueOps.set(key, v);
            if (time > 0) redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[{}]失败, value[{}]", key, v, t);
        }
        return false;
    }

    /**
     * 缓存value操作
     *
     * @param k
     * @param v
     * @param time
     * @param unit
     * @return boolean
     * @Author 科帮网
     * @Date 2017年12月23日 更新日志
     * 2017年12月23日  科帮网  首次创建
     */
    public boolean cacheValue(String k, Serializable v, long time, TimeUnit unit) {
        String key = KEY_PREFIX_VALUE + k;
        try {
            ValueOperations<Serializable, Serializable> valueOps = redisTemplate.opsForValue();
            valueOps.set(key, v);
            if (time > 0) redisTemplate.expire(key, time, unit);
            return true;
        } catch (Throwable t) {
            logger.error("缓存[{}]失败, value[{}]", key, v, t);
        }
        return false;
    }

    /**
     * 缓存value操作
     *
     * @param k
     * @param v
     * @return
     */
    public boolean cacheValue(String k, Serializable v) {
        return cacheValue(k, v, -1);
    }

    /**
     * 判断缓存是否存在
     *
     * @param k
     * @return
     */
    public boolean containsValueKey(String k) {
        String key = KEY_PREFIX_VALUE + k;
        try {
            return redisTemplate.hasKey(key);
        } catch (Throwable t) {
            logger.error("判断缓存存在失败key[" + key + ", error[" + t + "]");
        }
        return false;
    }

    /**
     * 获取缓存
     *
     * @param k
     * @return
     */
    public Serializable getValue(String k) {
        try {
            ValueOperations<Serializable, Serializable> valueOps = redisTemplate.opsForValue();
            return valueOps.get(KEY_PREFIX_VALUE + k);
        } catch (Throwable t) {
            logger.error("获取缓存失败key[" + KEY_PREFIX_VALUE + k + ", error[" + t + "]");
        }
        return null;
    }

    /**
     * 移除缓存
     *
     * @param k
     * @return
     */
    public boolean removeValue(String k) {
        String key = KEY_PREFIX_VALUE + k;
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Throwable t) {
            logger.error("获取缓存失败key[" + key + ", error[" + t + "]");
        }
        return false;
    }

    /**
     * 递增
     *
     * @param k
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String k, long delta) {
        String key = KEY_PREFIX_VALUE + k;
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param k     键
     * @param delta 要减少几(小于0)
     * @return long
     */
    public long decr(String k, long delta) {
        String key = KEY_PREFIX_VALUE + k;
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * 根据给定的布隆过滤器添加值
     */
    public <T> void addByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            redisTemplate.opsForValue().setBit(key, i, true);
        }
    }

    /**
     * 根据给定的布隆过滤器判断值是否存在
     */
    public <T> boolean includeByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            if (!redisTemplate.opsForValue().getBit(key, i)) {
                return false;
            }
        }
        return true;
    }
}
