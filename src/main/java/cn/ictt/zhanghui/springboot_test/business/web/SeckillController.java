package cn.ictt.zhanghui.springboot_test.business.web;

import cn.ictt.zhanghui.springboot_test.business.service.SeckillService;
import cn.ictt.zhanghui.springboot_test.common.queue.disruptor.DisruptorUtil;
import cn.ictt.zhanghui.springboot_test.common.queue.disruptor.SeckillEvent;
import cn.ictt.zhanghui.springboot_test.common.queue.jvm.arrayqueue.SeckillArrayQueue;
import cn.ictt.zhanghui.springboot_test.common.queue.kafka.KafkaSender;
import cn.ictt.zhanghui.springboot_test.common.queue.mq.MqMessageSend;
import cn.ictt.zhanghui.springboot_test.common.queue.redis.RedisSender;
import cn.ictt.zhanghui.springboot_test.common.redis.RedisService;
import cn.ictt.zhanghui.springboot_test.business.pojo.domain.Goods;
import cn.ictt.zhanghui.springboot_test.base.resp.ResponseInfo;
import cn.ictt.zhanghui.springboot_test.base.util.cipher.DoubleUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author ZhangHui
 * @version 1.0
 * @className SeckillController
 * @description 秒杀测试接口
 * @date 2020/3/10
 */
@Api(tags = "秒杀的实现方式")
@Controller
public class SeckillController {

    private CountDownLatch lock;

    private CyclicBarrier barrier;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int cpuNum = Runtime.getRuntime().availableProcessors();

    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(100, 150, 101, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100000));

    @Autowired
    private SeckillService seckillService;

    @Autowired(required = false)
    private MqMessageSend mqMessageSend;

    @Autowired(required = false)
    private RedisService redisService;

    @Autowired(required = false)
    private KafkaSender kafkaSender;

    @Autowired(required = false)
    private RedisSender redisSender;

    @Value("${tengxun.verfycode.url}")
    private String url;
    @Value("${tengxun.verfycode.appid}")
    private String aid;
    @Value("${tengxun.verfycode.secretkey}")
    private String appSecretKey;

    /*
     * 初始化还原操作
     * @param customNum
     * @param goodsId
     */
    private void init(int customNum, int goodsId) {
        lock = new CountDownLatch(customNum);
        seckillService.reset(goodsId);
        /**
         * 开启新线程之前，将RequestAttributes对象设置为子线程共享
         * 这里仅仅是为了测试，否则 IPUtils 中获取不到 request 对象
         * 用到限流注解的测试用例，都需要加一下两行代码
         */
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(sra, true);
        redisService.cacheValue(String.valueOf(goodsId), null);

        logger.info("当前" + customNum + "用户在线，秒杀开始！");
    }

    /**
     * 进入秒杀首页
     */
    @GetMapping("/seckill/index")
    public String seckillPage() {
        return "seckill/index";
    }

    /**
     * 获取秒杀商品信息
     */
    @GetMapping("/seckill/getlist")
    @ResponseBody
    public ResponseInfo seckilllist() {
        List<Goods> goodsList = seckillService.findAll();
        return ResponseInfo.OK(goodsList);
    }

    @GetMapping("/seckill/{id}")
    public String goodsDetail(@PathVariable("id") int id) {
        return "seckill/goods" + id;
    }

    @ApiOperation(value = "生成商品页面", notes = "根据模板生成商品详情页面")
    @ApiImplicitParam(name = "goodsId", dataType = "int", paramType = "query", required = true)
    @PostMapping("/createhtml")
    @ResponseBody
    public ResponseInfo createhtml(@RequestParam("goodsId") Integer goodsId) {
        if (seckillService.creatHtml(goodsId)) {
            return ResponseInfo.OK();
        }
        return ResponseInfo.FAIL();
    }

    @ApiOperation(value = "数据库悲观锁测试", notes = "使用数据库悲观锁实现秒杀")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customNum", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "goodsId", dataType = "int", paramType = "query", required = true)
    })
    @PostMapping("/buyGoods_pe")
    @ResponseBody
    public ResponseInfo buyGoods(@RequestParam("customNum") Integer customNum, @RequestParam("goodsId") Integer goodsId) {
        init(customNum, goodsId);
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < customNum; i++) {
            Thread thread = new Thread(() -> {
                seckillService.doSeckill_pem(goodsId);
                lock.countDown();
            }, "thread" + i);
            threadPool.execute(thread);
        }
        try {
            lock.await();
        } catch (InterruptedException e) {
            logger.error("countdownlatch等待期间被interrupt:" + e.getMessage(), e);
            return ResponseInfo.FAIL();
        }
        watch.stop();
        logger.info("{}名用户抢购商品{}共耗时{}毫秒", customNum, goodsId, watch.getTotalTimeMillis());
        return ResponseInfo.OK();
    }

    @ApiOperation(value = "数据库乐观锁测试", notes = "使用数据库乐观锁实现秒杀")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customNum", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "goodsId", dataType = "int", paramType = "query", required = true)
    })
    @PostMapping("/buyGoods_opt")
    @ResponseBody
    public ResponseInfo buyGoods_opt(@RequestParam("customNum") Integer customNum, @RequestParam("goodsId") Integer goodsId) {
        init(customNum, goodsId);
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < customNum; i++) {
            Thread thread = new Thread(() -> {
                seckillService.doSeckill_opt(goodsId);
                lock.countDown();
            }, "thread" + i);
            threadPool.execute(thread);
        }
        try {
            lock.await();
        } catch (InterruptedException e) {
            logger.error("countdownlatch等待期间被interrupt:" + e.getMessage(), e);
            return ResponseInfo.FAIL();
        }
        watch.stop();
        logger.info("{}名用户抢购商品{}共耗时{}毫秒", customNum, goodsId, watch.getTotalTimeMillis());
        return ResponseInfo.OK();
    }

    @ApiOperation(value = "进程内普通阻塞队列测试", notes = "使用普通队列实现秒杀")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customNum", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "goodsId", dataType = "int", paramType = "query", required = true)
    })
    @PostMapping("/buyGoods_blockqueue")
    @ResponseBody
    public ResponseInfo buyGoods_blockqueue(@RequestParam("customNum") Integer customNum, @RequestParam("goodsId") Integer goodsId) {
        init(customNum, goodsId);
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < customNum; i++) {
            Thread thread = new Thread(() -> {
                // 线程执行任务
                SeckillEvent seckillEvent = new SeckillEvent();
                seckillEvent.setSeckill_id(goodsId);
                SeckillArrayQueue.getSeckillQueue().produce(seckillEvent);
                lock.countDown();
            }, "thread" + i);
            threadPool.execute(thread);
        }
        try {
            lock.await();
        } catch (InterruptedException e) {
            logger.error("countdownlatch等待期间被interrupt:" + e.getMessage(), e);
            return ResponseInfo.FAIL();
        }
        watch.stop();
        logger.info("{}名用户抢购商品{}共耗时{}毫秒", customNum, goodsId, watch.getTotalTimeMillis());
        return ResponseInfo.OK();
    }

    @ApiOperation(value = "进程内队列disrupt测试", notes = "使用disrupt队列实现秒杀")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customNum", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "goodsId", dataType = "int", paramType = "query", required = true)
    })
    @PostMapping("/buyGoods_disrupt")
    @ResponseBody
    public ResponseInfo buyGoods_disrupt(@RequestParam("customNum") Integer customNum, @RequestParam("goodsId") Integer goodsId) {
        init(customNum, goodsId);
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < customNum; i++) {
            Thread thread = new Thread(() -> {
                // 线程执行任务
                SeckillEvent seckillEvent = new SeckillEvent();
                seckillEvent.setSeckill_id(goodsId);
                DisruptorUtil.produce(seckillEvent);
                lock.countDown();
            }, "thread" + i);
            threadPool.execute(thread);
        }
        try {
            lock.await();
        } catch (InterruptedException e) {
            logger.error("countdownlatch等待期间被interrupt:" + e.getMessage(), e);
            return ResponseInfo.FAIL();
        }
        watch.stop();
        logger.info("{}名用户抢购商品{}共耗时{}毫秒", customNum, goodsId, watch.getTotalTimeMillis());
        return ResponseInfo.OK();
    }

    @ApiOperation(value = "分布式mq秒杀", notes = "基于activemq实现秒杀任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customNum", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "goodsId", dataType = "int", paramType = "query", required = true)
    })
    @PostMapping("/buyGoods_mq")
    @ResponseBody
    public ResponseInfo buyGoods_mq(@RequestParam("customNum") Integer customNum, @RequestParam("goodsId") Integer goodsId) {
        init(customNum, goodsId);
        //下面这段注释用于网页秒杀之前会进行验证码验证，防止恶意调用接口，需要前端js回调函数中的ticket和randstr
//        HttpMethod method =HttpMethod.POST;
//        MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
//        params.add("aid", aid);
//        params.add("AppSecretKey", appSecretKey);
//        params.add("Ticket", ticket);
//        params.add("Randstr", randstr);
//        params.add("UserIP", IPUtils.getIpAddr());
//        String msg = httpClient.client(url,method,params);
//        /**
//         * response: 1:验证成功，0:验证失败，100:AppSecretKey参数校验错误[required]
//         * evil_level:[0,100]，恶意等级[optional]
//         * err_msg:验证错误信息[optional]
//         */
//        //{"response":"1","evil_level":"0","err_msg":"OK"}
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < customNum; i++) {
            Thread thread = new Thread(() -> {
                if (redisService.getValue(String.valueOf(goodsId)) == null) {
                    // 线程执行任务
                    mqMessageSend.sendQueue("seckill-queue", String.valueOf(goodsId));
                }
                lock.countDown();
            }, "thread" + i);
            threadPool.execute(thread);
        }
        try {
            lock.await();
        } catch (InterruptedException e) {
            logger.error("countdownlatch等待期间被interrupt:" + e.getMessage(), e);
            return ResponseInfo.FAIL();
        }
        watch.stop();
        logger.info("{}名用户抢购商品{}共耗时{}毫秒", customNum, goodsId, watch.getTotalTimeMillis());
        return ResponseInfo.OK();
    }

    @ApiOperation(value = "分布式kafka秒杀", notes = "基于kafka实现秒杀任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customNum", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "goodsId", dataType = "int", paramType = "query", required = true)
    })
    @PostMapping("/buyGoods_kafka")
    @ResponseBody
    public ResponseInfo buyGoods_kafka(@RequestParam("customNum") Integer customNum, @RequestParam("goodsId") Integer goodsId) {
        init(customNum, goodsId);
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < customNum; i++) {
            Thread thread = new Thread(() -> {
                if (redisService.getValue(String.valueOf(goodsId)) == null) {
                    // 线程执行任务
                    kafkaSender.send("topic.seckill", String.valueOf(goodsId));
                }
                lock.countDown();
            }, "thread" + i);
            threadPool.execute(thread);
        }
        try {
            lock.await();
        } catch (InterruptedException e) {
            logger.error("countdownlatch等待期间被interrupt:" + e.getMessage(), e);
            return ResponseInfo.FAIL();
        }
        watch.stop();
        logger.info("{}名用户抢购商品{}共耗时{}毫秒", customNum, goodsId, watch.getTotalTimeMillis());
        return ResponseInfo.OK();
    }

    @ApiOperation(value = "分布式redis消息监听", notes = "基于redis发步订阅队列实现秒杀任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customNum", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "goodsId", dataType = "int", paramType = "query", required = true)
    })
    @PostMapping("/buyGoods_redisqueue")
    @ResponseBody
    public ResponseInfo buyGoods_redisqueue(@RequestParam("customNum") Integer customNum, @RequestParam("goodsId") Integer goodsId) {
        init(customNum, goodsId);
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < customNum; i++) {
            Thread thread = new Thread(() -> {
                if (redisService.getValue(String.valueOf(goodsId)) == null) {
                    // 线程执行任务
                    redisSender.sendChannelMess("seckill-channel", String.valueOf(goodsId));
                }
                lock.countDown();
            }, "thread" + i);
            threadPool.execute(thread);
        }
        try {
            lock.await();
        } catch (InterruptedException e) {
            logger.error("countdownlatch等待期间被interrupt:" + e.getMessage(), e);
            return ResponseInfo.FAIL();
        }
        watch.stop();
        logger.info("{}名用户抢购商品{}共耗时{}毫秒", customNum, goodsId, watch.getTotalTimeMillis());
        return ResponseInfo.OK();
    }

    @ApiOperation(value = "分布式redis锁秒杀", notes = "基于redis锁实现秒杀任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customNum", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "goodsId", dataType = "int", paramType = "query", required = true)
    })
    @PostMapping("/buyGoods_redissonlock")
    @ResponseBody
    public ResponseInfo buyGoods_redissonlock(@RequestParam("customNum") Integer customNum, @RequestParam("goodsId") Integer goodsId) {
        init(customNum, goodsId);
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < customNum; i++) {
            Thread thread = new Thread(() -> {
                // 线程执行任务
                seckillService.doSeckill_redissonlock(goodsId);
                lock.countDown();
            }, "thread" + i);
            threadPool.execute(thread);
        }
        try {
            lock.await();
        } catch (InterruptedException e) {
            logger.error("countdownlatch等待期间被interrupt:" + e.getMessage(), e);
            return ResponseInfo.FAIL();
        }
        watch.stop();
        logger.info("{}名用户抢购商品{}共耗时{}毫秒", customNum, goodsId, watch.getTotalTimeMillis());
        return ResponseInfo.OK();
    }

    @ApiOperation(value = "分布式zk锁秒杀", notes = "基于zookeeper锁实现秒杀任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customNum", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "goodsId", dataType = "int", paramType = "query", required = true)
    })
    @PostMapping("/buyGoods_zklock")
    @ResponseBody
    public ResponseInfo buyGoods_zklock(@RequestParam("customNum") Integer customNum, @RequestParam("goodsId") Integer goodsId) {
        init(customNum, goodsId);
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < customNum; i++) {
            Thread thread = new Thread(() -> {
                // 线程执行任务
                seckillService.doSeckill_zklock(goodsId);
                lock.countDown();
            }, "thread" + i);
            threadPool.execute(thread);
        }
        try {
            lock.await();
        } catch (InterruptedException e) {
            logger.error("countdownlatch等待期间被interrupt:" + e.getMessage(), e);
            return ResponseInfo.FAIL();
        }
        watch.stop();
        logger.info("{}名用户抢购商品{}共耗时{}毫秒", customNum, goodsId, watch.getTotalTimeMillis());
        return ResponseInfo.OK();
    }

    @ApiOperation(value = "分布式redis锁抢红包", notes = "基于redis锁实现抢红包")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customNum", dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "packageId", dataType = "int", paramType = "query", required = true)
    })
    @PostMapping("/redpackage")
    @ResponseBody
    public ResponseInfo redpackage(@RequestParam("customNum") Integer customNum, @RequestParam("packageId") Integer packageId) {
        if (barrier != null) {
            barrier.reset();
        } else {
            barrier = new CyclicBarrier(100);
        }
        lock = new CountDownLatch(customNum);
        //设置红包份数
        redisService.cacheValue(packageId + "-num", 10);

        //设置红包份数
        redisService.cacheValue(packageId + "-restpeople", 10);

        //设置红包金额，单位分,10s后过期，实现红包过期退款，比如设置过期时间24h
        redisService.cacheValue(packageId + "-amount", 10000, 10, TimeUnit.SECONDS);

        /**
         * 除了监听redis key监听事件还可以加入延迟队列 24s秒过期，实际可以设置24*60*60秒，也就是一天
         * 还可以使用redis高性能延时队列
         */
//        RedPacketMessage message = new RedPacketMessage(redPacketId,24);
//        RedPacketQueue.getQueue().produce(message);

        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < customNum; i++) {
            Thread thread = new Thread(() -> {
                try {
                    barrier.await();
                } catch (Exception e) {
                    logger.error("抢红包线程等待期间出现异常：" + e.getMessage(), e);
                }
                long packageNum = redisService.decr(packageId + "-num", 1);
                if (packageNum >= 0) {
                    long acquireMoney = seckillService.doRedPackage(packageId);
                    double money = DoubleUtil.divide(Double.valueOf(acquireMoney), 100.0);
                    logger.info("用户" + Thread.currentThread().getName() + "成功抢到金额{}元", money);
                }
                // 线程执行任务
                lock.countDown();
            }, "red-package-thread" + i);
            threadPool.execute(thread);
        }
        try {
            lock.await();
        } catch (InterruptedException e) {
            logger.error("countdownlatch等待期间被interrupt:" + e.getMessage(), e);
            return ResponseInfo.FAIL();
        }
        watch.stop();
        logger.info("{}名用户抢红包{}共耗时{}毫秒，剩余红包金额{}", customNum, packageId, watch.getTotalTimeMillis(), redisService.getValue(packageId + "-amount"));
        return ResponseInfo.OK();
    }
}