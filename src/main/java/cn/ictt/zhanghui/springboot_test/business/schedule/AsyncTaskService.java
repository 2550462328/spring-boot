package cn.ictt.zhanghui.springboot_test.business.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;
@Slf4j
@Component
public class AsyncTaskService {

    @Autowired
   private StringRedisTemplate stringRedisTemplate;
    /*
    返回Future获取线程执行状态
     */
    @Async("taskExecutor")
    public Future<String> doTask1() throws InterruptedException{
        log.info("开始做任务一");
        long start = System.currentTimeMillis();
        log.info(stringRedisTemplate.randomKey());
        long end = System.currentTimeMillis();
        log.info("完成任务一，耗时：" + (end - start) + "毫秒");
        return new AsyncResult<>("任务一完成");
    }

    @Async("taskExecutor")
    public Future<String> doTask2()throws InterruptedException{
        log.info("开始做任务二");
        long start = System.currentTimeMillis();
        log.info(stringRedisTemplate.randomKey());
        long end = System.currentTimeMillis();
        log.info("完成任务二，耗时：" + (end - start) + "毫秒");
        return new AsyncResult<>("任务一完成");
    }
    @Async("taskExecutor")
    public Future<String> doTask3()throws InterruptedException{
        log.info("开始做任务三");
        long start = System.currentTimeMillis();
        log.info(stringRedisTemplate.randomKey());
        long end = System.currentTimeMillis();
        log.info("完成任务三，耗时：" + (end - start) + "毫秒");
        return new AsyncResult<>("任务一完成");
    }
}
