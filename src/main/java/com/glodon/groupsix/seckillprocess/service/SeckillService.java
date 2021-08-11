package com.glodon.groupsix.seckillprocess.service;

import com.glodon.groupsix.seckillprocess.service.mq.SendMessage;
import com.glodon.groupsix.seckillprocess.utils.CodeMsg;
import com.glodon.groupsix.seckillprocess.utils.JedisUtil;
import com.glodon.groupsix.seckillprocess.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class SeckillService {

    @Autowired
    JedisUtil jedisUtil;

    @Autowired
    SendMessage sendMessage;

    private static ReentrantLock lock = new ReentrantLock();

    private ConcurrentHashMap<String, BlockingQueue<String>> seckillQueue = new ConcurrentHashMap<>();

    public boolean candidateSeckill(String goodsCode, String phone){

        return false;
    }

    /**
     * v1.0版本
     * 依靠可重入锁，多次校验与写入基于redis
     * @param commodityCode
     * @param phone
     * @return Result
     */
    public Result seckill(String commodityCode, String phone){
        String phoneKey = commodityCode+"_phone";
        lock.lock();
        try {
            if (jedisUtil.sismember(phoneKey, phone)){
                return new Result(CodeMsg.REPEATE_MIAOSHA);
            }
            Integer surplusStock = Integer.parseInt(jedisUtil.get(commodityCode));
            if (surplusStock == 0){
                return new Result(CodeMsg.MIAOSHA_OVER_ERROR);
            }
            surplusStock-=1;
            jedisUtil.sadd(phoneKey, phone);
            jedisUtil.set(commodityCode, String.valueOf(surplusStock));
        } finally {
            lock.unlock();
        }
        return new Result(CodeMsg.SUCCESS);
    }

    public Result seckillV2(String commodityCode, String phone){
        return null;
    }
}
