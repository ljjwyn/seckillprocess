package com.glodon.groupsix.seckillprocess.service.mq;

import com.glodon.groupsix.seckillprocess.mapper.TSeckillRecordDao;
import com.glodon.groupsix.seckillprocess.models.vo.TSeckillRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RabbitListener(queues = "RecordDirectQueue")//监听的队列名称 TestDirectQueue
public class ReceiverRecordSQLService {
    @Autowired
    TSeckillRecordDao tSeckillRecordDao;

    @RabbitHandler
    public void process(TSeckillRecord tSeckillRecord) {
        // 高并发测试记得注释掉这些日志
        log.info("2、收到抢购记录消息  :{} ", tSeckillRecord.toString());
        tSeckillRecordDao.insertSelective(tSeckillRecord);
    }
}
