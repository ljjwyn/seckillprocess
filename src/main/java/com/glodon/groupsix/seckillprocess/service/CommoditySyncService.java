package com.glodon.groupsix.seckillprocess.service;

import com.glodon.groupsix.seckillprocess.utils.LettuceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommoditySyncService {
    @Autowired
    LettuceUtil lettuceUtil;

    public void letCommodityPutaway(String commodityCode, String surplusStock, Long expireTime) {
        log.warn("startTime:{}",expireTime);
        if (expireTime==null){
            String startTimeKey = commodityCode+"_startTime";
            lettuceUtil.set(commodityCode, surplusStock);
            if (lettuceUtil.hasKey(startTimeKey)){
                lettuceUtil.del(startTimeKey);
            }
            log.info("剩余时间为null，仅仅更新库存");
            return;
        }
        log.info("商品管理系统请求：上架商品：{}，库存为：{}", commodityCode, surplusStock);
        log.warn("距离商品：{}，开始秒杀还有：{}分钟", commodityCode, expireTime/60000);
        lettuceUtil.set(commodityCode, surplusStock);
        lettuceUtil.setValueAndExpire(commodityCode+"_startTime", surplusStock, expireTime);
    }
}
