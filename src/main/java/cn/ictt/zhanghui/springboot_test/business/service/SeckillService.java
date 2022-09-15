package cn.ictt.zhanghui.springboot_test.business.service;

import cn.ictt.zhanghui.springboot_test.business.pojo.domain.Goods;

import java.util.List;

public interface SeckillService {

    boolean doSeckill(int goodsId);

    boolean doSeckill_pem(int goodsId);

    boolean doSeckill_opt(int goodsId);

    boolean doSeckill_redissonlock(int goodsId);

    boolean doSeckill_zklock(int goodsId);

    void reset(int goodsId);

    List<Goods> findAll();

    boolean creatHtml(int goodsId);

    long doRedPackage(int packageId);
}
