package com.glodon.groupsix.seckillprocess.service;

import com.glodon.groupsix.seckillprocess.mapper.TSeckillRecordDao;
import com.glodon.groupsix.seckillprocess.models.vo.GetCountAndUser;
import com.glodon.groupsix.seckillprocess.models.vo.TSeckillRecord;
import com.glodon.groupsix.seckillprocess.models.vo.TSeckillRecordInfo;
import com.glodon.groupsix.seckillprocess.service.mq.SendMessage;
import com.glodon.groupsix.seckillprocess.utils.CodeMsg;
import com.glodon.groupsix.seckillprocess.utils.JedisUtil;
import com.glodon.groupsix.seckillprocess.utils.LettuceUtil;
import com.glodon.groupsix.seckillprocess.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
public class SeckillService {

    @Autowired
    JedisUtil jedisUtil;

    @Autowired
    SendMessage sendMessage;

    @Autowired
    LettuceUtil lettuceUtil;

    @Autowired
    TSeckillRecordService tSeckillRecordService;

    @Autowired
    TSeckillRecordDao tSeckillRecordDao;

    private static final ReentrantLock lock = new ReentrantLock();

    /**
     * v1.0版本
     * 依靠可重入锁，多次校验与写入基于redis,停止维护
     * @param commodityCode 商品编码
     * @param phone 用户手机号
     * @param commodityName 商品名
     * @param seckillPrice 秒杀价格
     * @return Result
     */
    @Deprecated
    public Result<CodeMsg> seckill(String commodityCode, String phone, String commodityName, String seckillPrice){
        String phoneKey = commodityCode+"_phone";
        lock.lock();
        try {
            if (jedisUtil.sismember(phoneKey, phone)){
                log.info("v1.0==失败！重复抢购");
                tSeckillRecordService.insertSelective(new TSeckillRecord(null, phone, commodityCode
                        , commodityName, seckillPrice, new Date(), "失败"));
                return new Result(CodeMsg.REPEATE_MIAOSHA);
            }
            int surplusStock = Integer.parseInt(jedisUtil.get(commodityCode));
            if (surplusStock == 0){
                log.info("v1.0==失败！抢完了，库存为0");
                tSeckillRecordService.insertSelective(new TSeckillRecord(null, phone, commodityCode
                        , commodityName, seckillPrice, new Date(), "失败"));
                return new Result(CodeMsg.MIAOSHA_OVER_ERROR);
            }
            surplusStock-=1;
            log.info("v1.0======获得抢购机会:库存剩余：{}", surplusStock);
            jedisUtil.sadd(phoneKey, phone);
            jedisUtil.set(commodityCode, String.valueOf(surplusStock));
            // 数据库插入
            tSeckillRecordService.insertSelective(new TSeckillRecord(null, phone, commodityCode
                    , commodityName, seckillPrice, new Date(), "成功"));
        } finally {
            lock.unlock();
        }
        return new Result(CodeMsg.SUCCESS);
    }

    /**
     * v2.0版本
     * 使用lettuce操作redis。
     * lettuce天生线程安全锁的粒度可以适度降低
     * @param commodityCode 商品编码
     * @param phone 用户手机号
     * @param commodityName 商品名
     * @param seckillPrice 秒杀价格
     * @return Result
     */
    public Result<CodeMsg> seckillV2(String commodityCode, String phone, String commodityName, String seckillPrice){
        String phoneKey = commodityCode+"_phone";
        lock.lock();
        try {
            if (lettuceUtil.contains(phoneKey, phone)){
                // 数据库插入
                log.info("v2.0==失败！重复抢购");
                tSeckillRecordService.insertSelective(new TSeckillRecord(null, phone, commodityCode
                    , commodityName, seckillPrice, new Date(), "失败"));
                return new Result(CodeMsg.REPEATE_MIAOSHA);
            }
            int surplusStock = Integer.parseInt(lettuceUtil.get(commodityCode));
            if (surplusStock == 0){
                // 数据库插入
                log.info("v2.0==失败！抢完了，库存为0");
                tSeckillRecordService.insertSelective(new TSeckillRecord(null, phone, commodityCode
                    , commodityName, seckillPrice, new Date(), "失败"));
                return new Result(CodeMsg.MIAOSHA_OVER_ERROR);
            }
            surplusStock-=1;
            log.info("v2.0======获得抢购机会:库存剩余：{}", surplusStock);
            lettuceUtil.sadd(phoneKey, phone);
            lettuceUtil.set(commodityCode, String.valueOf(surplusStock));
            // 数据库插入
            tSeckillRecordService.insertSelective(new TSeckillRecord(null, phone, commodityCode
                    , commodityName, seckillPrice, new Date(), "成功"));

        }finally {
            lock.unlock();
        }
        return new Result(CodeMsg.SUCCESS);
    }

    /**
     * v3.0版本
     * 使用lettuce操作redis。
     * 往数据库存record使用消息队列
     * 操作Redis没有使用消息队列
     * @param commodityCode 商品编码
     * @param phone 用户手机号
     * @param commodityName 商品名
     * @param seckillPrice 秒杀价格
     * @return Result
     */
    public Result<CodeMsg> seckillV3(String commodityCode, String phone, String commodityName, String seckillPrice){
        String phoneKey = commodityCode+"_phone";
        lock.lock();
        TSeckillRecord tSeckillRecord = new TSeckillRecord(null, phone, commodityCode
                , commodityName, seckillPrice, new Date(), "秒杀中");
        if (lettuceUtil.contains(phoneKey, phone)){
            // 将存库信息压入异步消息队列
            log.info("v3.0==失败！重复抢购");
            tSeckillRecord.setStatus("失败");
            sendMessage.sendRecordSQLMessage(tSeckillRecord);
            return new Result(CodeMsg.REPEATE_MIAOSHA);
        }
        int surplusStock = Integer.parseInt(lettuceUtil.get(commodityCode));
        if (surplusStock == 0){
            // 将存库信息压入异步消息队列
            log.info("v3.0==失败！抢完了，库存为0");
            tSeckillRecord.setStatus("失败");
            sendMessage.sendRecordSQLMessage(tSeckillRecord);
            return new Result(CodeMsg.MIAOSHA_OVER_ERROR);
        }
        try {
            surplusStock-=1;
            log.info("v3.0======获得抢购机会:库存剩余：{}", surplusStock);
            lettuceUtil.sadd(phoneKey, phone);
            lettuceUtil.set(commodityCode, String.valueOf(surplusStock));
            // 将存库信息压入异步消息队列
            tSeckillRecord.setStatus("成功");
            sendMessage.sendRecordSQLMessage(tSeckillRecord);
        }finally {
            lock.unlock();
        }
        return new Result(CodeMsg.SUCCESS);
    }

    /**
     * v4.0版本
     * redis处理与数据库存储使用两组rabbitmq
     * 实现异步削峰
     * @param commodityCode 商品编码
     * @param phone 用户手机号
     * @param commodityName 商品名
     * @param seckillPrice 秒杀价格
     * @return Result
     */
    public Result<CodeMsg> seckillV4(String commodityCode, String phone, String commodityName, String seckillPrice){
        int surplusStock = Integer.parseInt(lettuceUtil.get(commodityCode));
        if (surplusStock == 0){
            sendMessage.sendRecordSQLMessage(new TSeckillRecord(null, phone, commodityCode
                    , commodityName, seckillPrice, new Date(), "失败"));
            return new Result(CodeMsg.MIAOSHA_OVER_ERROR);
        }
        String phoneKey = commodityCode+"_phone";
        if (lettuceUtil.contains(phoneKey, phone)){
            log.info("v4.0==失败！重复抢购");
            sendMessage.sendRecordSQLMessage(new TSeckillRecord(null, phone, commodityCode
                    , commodityName, seckillPrice, new Date(), "失败"));
            return new Result(CodeMsg.REPEATE_MIAOSHA);
        }
        TSeckillRecord tSeckillRecord = new TSeckillRecord(null, phone, commodityCode
                , commodityName, seckillPrice, new Date(), "秒杀中");
        // sendMessage.sendRecordSQLMessage(tSeckillRecord);
        sendMessage.sendSeckillMessage(tSeckillRecord);
        return new Result(CodeMsg.SUCCESS_SECKILLING);
    }

    /**
     * 已上架商品获取商品详情页的售出数量与当前秒杀成功者的手机号
     * 从redis拿
     * @param commodityCode 商品编号
     * @return Result
     */
    public Result<GetCountAndUser> getCountAndUser(String commodityCode) {
        String phoneKey = commodityCode+"_phone";
        GetCountAndUser countAndUser = new GetCountAndUser();
        String count = lettuceUtil.get(commodityCode);
        Set<String> userNumber = lettuceUtil.values(phoneKey);
        countAndUser.setCount(count);
        countAndUser.setUserNumber(userNumber);
        return new Result(countAndUser);
    }

    /**
     * 根据用户手机号获取用户秒杀记录
     * 对标秒杀记录页面
     * @param phone 手机号
     * @return Result
     */
    public Result<List<TSeckillRecord>> getRecordByPhone(String phone) {
        return new Result(tSeckillRecordDao.getRecordByPhone(phone));
    }

    /**
     * 已下架或售完商品获取商品详情页的售出数量与当前秒杀成功者的手机号
     * 从数据库拿
     * @param commodityId 商品编码
     * @return Result
     */
    public Result<TSeckillRecordInfo> getRecordByCommodityId(String commodityId) {
        List<String> recordByCommodityId = tSeckillRecordDao.getRecordByCommodityId(commodityId);
        int count = tSeckillRecordDao.getRecordCountByCommodityId(commodityId);
        TSeckillRecordInfo recordInfo = new TSeckillRecordInfo();
        recordInfo.setTSeckillRecords(recordByCommodityId);
        recordInfo.setCount(count);
        return new Result(recordInfo);
    }
}
