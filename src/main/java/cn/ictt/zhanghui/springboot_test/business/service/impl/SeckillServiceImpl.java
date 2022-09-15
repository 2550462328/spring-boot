package cn.ictt.zhanghui.springboot_test.business.service.impl;

import cn.ictt.zhanghui.springboot_test.base.exception.enums.BusinessResponseEnum;
import cn.ictt.zhanghui.springboot_test.business.pojo.domain.Goods;
import cn.ictt.zhanghui.springboot_test.business.pojo.domain.GoodsRepository;
import cn.ictt.zhanghui.springboot_test.business.service.SeckillService;
import cn.ictt.zhanghui.springboot_test.common.freemarker.TemplateService;
import cn.ictt.zhanghui.springboot_test.common.lock.RedissLockUtil;
import cn.ictt.zhanghui.springboot_test.common.lock.ZkLockUtil;
import cn.ictt.zhanghui.springboot_test.common.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhangHui
 * @version 1.0
 * @className SeckillServiceImpl
 * @description 这是描述信息
 * @date 2020/3/10
 */
@Slf4j
@Service("seckillService")
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private TemplateService templateService;

    @Autowired(required = false)
    private RedisService redisService;

    private Logger logger = LoggerFactory.getLogger(SeckillServiceImpl.class);

    //物品初始数量
    private static final int INITAL_NUM = 100;
    private static final int INITAL_VERSION = 1;

    /**
     * 根据商品编号和商品模板生成商品详情页面
     *
     * @param goodsId 商品编号
     * @return boolean 创建页面是否成功
     */
    @Override
    public boolean creatHtml(int goodsId) {
        Goods goods = goodsRepository.findById(goodsId);
        BusinessResponseEnum.INVAID_GOOD_NUMBER.assertNotNull(goods);

        Callable<String> createCall = () -> {
            try {
                templateService.createHtml(goods, "goods.flt", "goods" + goods.getId());
            } catch (Exception e) {
                logger.error("生成静态页面goods" + goods.getId() + ".htlm出现异常：" + e.getMessage(), e);
                return "fail to create html goods" + goods.getId() + ".html";
            }
            return "success";
        };
        FutureTask<String> callTask = new FutureTask<>(createCall);

        Thread goodsPageCreateThread = new Thread(callTask, "goodsPageCreateThread");

        goodsPageCreateThread.start();

        //callTask.get()会阻塞到线程返回结果
        try {
            if ("success".equals(callTask.get())) {
                return true;
            }
        } catch (Exception e) {
            logger.error("生成静态页面goods" + goods.getId() + ".html出现异常：" + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public List<Goods> findAll() {
        return goodsRepository.findAll();
    }

    /**
     * 正常减少商品数量的方法
     *
     * @param goodsId
     */
    @Override
    @Transactional
    public boolean doSeckill(int goodsId) {
        try {
            Goods good = goodsRepository.findById(goodsId);
            int num = good.getNum();
            if (num > 0) {
                goodsRepository.decreaseNumById(goodsId);
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 减少商品数量(悲观锁)
     * 耗时长
     *
     * @param goodsId
     */
    @Override
//    @Transactional
    public boolean doSeckill_pem(int goodsId) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Goods good = goodsRepository.findByIdWithLock(goodsId);
            int num = good.getNum();
            if (num > 0) {
                goodsRepository.decreaseNumById(goodsId);
                return true;
            }
        } catch (Exception e) {
            transactionManager.rollback(status);
        } finally {
            transactionManager.commit(status);
        }
        return false;
    }

    /**
     * 减少商品数量(乐观锁)
     * 在抢购人数过少时会出现少卖，耗时短
     *
     * @param goodsId
     */
    @Override
    @Transactional
    public boolean doSeckill_opt(int goodsId) {
        Goods good = goodsRepository.findById(goodsId);
        if (good.getNum() > 0) {
            goodsRepository.updateNumById(good);
            return true;
        }
        return false;
    }

    /**
     * 基于redis分布式锁实现秒杀
     * 效率和性能都还不错，没有发现超卖和少卖
     *
     * @param goodsId 商品编号
     * @return boolean 秒杀结果
     */
    @Override
    @Transactional
    public boolean doSeckill_redissonlock(int goodsId) {
        String key = "seckill_key" + goodsId;
        boolean lockRes;

        //获取redisson lock，在3s内限时获取，持有锁后在20s后自动释放，防止死锁，实际项目推荐设置
        lockRes = RedissLockUtil.tryLock(key, TimeUnit.SECONDS, 3, 20);

        try {
            if (lockRes) {
                Goods good = goodsRepository.findById(goodsId);
                int num = good.getNum();
                if (num > 0) {
                    goodsRepository.decreaseNumById(goodsId);
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("未知的异常：" + e.getMessage(), e);
        } finally {
            if (lockRes) {
                RedissLockUtil.unlock(key);
            }
        }
        return false;
    }

    /**
     * 基于zookeeper分布式锁实现秒杀
     * 实验正常在1000并发的时候就特别慢了，不仅慢，而且出现少卖，可能跟锁的失败策略有关
     *
     * @param goodsId 商品编号
     * @return boolean 秒杀结果
     */
    @Override
    @Transactional
    public boolean doSeckill_zklock(int goodsId) {
        boolean lockRes = false;

        try {
            //在5s内限时获取
            lockRes = ZkLockUtil.acquire(5, TimeUnit.SECONDS);
            if (lockRes) {
                Goods good = goodsRepository.findById(goodsId);
                int num = good.getNum();
                if (num > 0) {
                    goodsRepository.decreaseNumById(goodsId);
                    return true;
                }
            }
        } catch (Exception e) {
            logger.error("未知的异常：" + e.getMessage(), e);
        } finally {
            if (lockRes) {
                try {
                    ZkLockUtil.release();
                } catch (Exception e) {
                    logger.error("未知的异常：" + e.getMessage(), e);
                }
            }
        }
        return false;
    }

    /**
     * 基于redis分布式锁抢红包
     *
     * @param packageId
     * @return long 抢到的红包金额
     */
    @Override
    public long doRedPackage(int packageId) {
        String key = "package_key" + packageId;
        boolean lockRes;
        int acquireMoney = 0;
        //获取redisson lock，在3s内限时获取，持有锁后在20s后自动释放，防止死锁，实际项目推荐设置
        lockRes = RedissLockUtil.tryLock(key, TimeUnit.SECONDS, 3, 10);

        try {
            if (lockRes) {
                //restPeople返回是减掉1的后的值
                long restPeople = redisService.decr(packageId + "-restpeople", 1);
                if (restPeople < 0) {
                    acquireMoney = 0;
                } else if (restPeople == 0) {
                    acquireMoney = (int) redisService.getValue(packageId + "-amount");
                } else {
                    int restMoney = (int) redisService.getValue(packageId + "-amount");
                    Random random = new Random();
                    //随机范围：[1,剩余人均金额的两倍]
                    acquireMoney = random.nextInt((int) (restMoney / (restPeople + 1) * 2 - 1)) + 1;
                }
                if (acquireMoney != 0) {
                    redisService.decr(packageId + "-amount", acquireMoney);
                    /*
                     * 异步保存领取数据
                     */
                    /*
                     * 异步红包入账
                     */
                }
            } else {
                redisService.incr(packageId + "-num", 1);
            }

        } catch (Exception e) {
            logger.error("未知的异常：" + e.getMessage(), e);
        } finally {
            if (lockRes) {
                RedissLockUtil.unlock(key);
            }
        }
        return acquireMoney;
    }

    /**
     * 重置商品数量
     *
     * @param goodsId
     */
    @Override
    public void reset(int goodsId) {
        Goods good = goodsRepository.findById(goodsId);
        good.setNum(INITAL_NUM);
        good.setVersion(INITAL_VERSION);
        goodsRepository.save(good);
    }

}
