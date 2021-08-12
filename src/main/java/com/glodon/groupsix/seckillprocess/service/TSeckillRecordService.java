package com.glodon.groupsix.seckillprocess.service;

import com.glodon.groupsix.seckillprocess.mapper.TSeckillRecordDao;
import com.glodon.groupsix.seckillprocess.models.vo.TSeckillRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TSeckillRecordService {
    @Autowired
    TSeckillRecordDao tSeckillRecordDao;

    public void insertSelective(TSeckillRecord tSeckillRecord){
        log.info("写入秒杀记录，秒杀手机号：{}，商品名：{}",tSeckillRecord.getPhone(),tSeckillRecord.getCommodityName());
        tSeckillRecordDao.insertSelective(tSeckillRecord);
    }

    public void updateByPrimaryKeySelective(TSeckillRecord tSeckillRecord){
        log.info("更新秒杀结果，秒杀手机号：{}，商品名：{}，秒杀结果：{}",
                tSeckillRecord.getPhone(),tSeckillRecord.getCommodityName(), tSeckillRecord.getStatus());
        tSeckillRecordDao.updateByPrimaryKeySelective(tSeckillRecord);
    }
}
