package cn.ictt.zhanghui.springboot_test.base;

import cn.ictt.zhanghui.springboot_test.SpringbootTestApplicationTests;
import cn.ictt.zhanghui.springboot_test.common.lock.jedis.RedisDistributedLock;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Warmup;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ZhangHui
 * @version 1.0
 * @className JedisTest
 * @description jedis的相关测试
 * @date 2020/8/20
 */
public class JedisTest extends SpringbootTestApplicationTests {

    static int good_num = 10;

    /**
     * 基于jmh测试，测试一秒钟方法可以执行多少次（TPS + QPS）
     * @author ZhangHui
     * @date 2020/9/18
     */
    @Benchmark
    @Fork(5)
    @Measurement(iterations = 3,time = 3)
    @Warmup(iterations = 1,time = 3)
    public void test_xingneng(){
        Jedis jedis = new Jedis("localhost",6379,100000);

        int i = 0;

        try {
                i++;
                jedis.set("test" + i,i+"");
        } finally {
            jedis.close();
        }
    }

    @Test
    public void test_redislock(){
        Jedis jedis = new Jedis("localhost",6379,100000);

        CountDownLatch downLatch = new CountDownLatch(1);

        String lockKey = "goods_key";

        RedisDistributedLock redisLock = new RedisDistributedLock(jedis,lockKey);

        AtomicInteger count = new AtomicInteger(0);

        Thread[] threads = new Thread[10];

        for(int i = 0; i < 10;i++){
            Thread thread = new Thread(()->{
                String requiredToken = null;
                count.incrementAndGet();
                try {
                    requiredToken = redisLock.acquire();

                    System.out.println(requiredToken);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(requiredToken != null){
                        redisLock.release(requiredToken);
                    }
                }
            });
            threads[i] = thread;
        }

        for(Thread thread : threads){
            thread.start();
        }

        for(Thread thread : threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        jedis.close();
//        while(count.get() != 10){
//            // DO NOTHING
//        }
//        downLatch.countDown();
    }
}
