package com.glodon.groupsix.seckillprocess.service.mq;

import com.glodon.groupsix.seckillprocess.models.vo.TSeckillRecord;
import com.glodon.groupsix.seckillprocess.utils.LettuceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
@RabbitListener(queues = "TestDirectQueue")//监听的队列名称 TestDirectQueue
public class ReceiverSeckillService {
    @Autowired
    LettuceUtil lettuceUtil;

    @Autowired
    SendMessage sendMessage;

    private static ReentrantLock lock = new ReentrantLock();

    @RabbitHandler
    public void process(TSeckillRecord tSeckillRecord) {
        // 高并发测试记得注释掉这些日志
        log.info("1、收到用户抢购消息  :{} ", tSeckillRecord.toString());
        String key = tSeckillRecord.getCommodityId();
        String phoneKey = key+"_phone";
        asyncRedisProcess(tSeckillRecord, phoneKey);
    }

    public void asyncRedisProcess(TSeckillRecord tSeckillRecord, String phoneKey){
        String key = tSeckillRecord.getCommodityId();
        String value = tSeckillRecord.getPhone();
        //TODO 这里写个持久化日志。
        if (lettuceUtil.contains(phoneKey, value)){
            log.info("v4.0==失败！重复抢购");
            tSeckillRecord.setStatus("失败");
            sendMessage.sendRecordSQLMessage(tSeckillRecord);
            return ;
        }
        int surplusStock = Integer.parseInt(lettuceUtil.get(key));
        if (surplusStock == 0){
            log.info("v4.0==失败！抢完了，库存为0");
            tSeckillRecord.setStatus("失败");
            sendMessage.sendRecordSQLMessage(tSeckillRecord);
            return ;
        }
        // TODO 还是上锁，想想解决方案。
        lock.lock();
        try {
            surplusStock-=1;
            log.info("v4.0======获得抢购机会:库存剩余：{}", surplusStock);
            lettuceUtil.sadd(phoneKey, value);
            lettuceUtil.set(key, String.valueOf(surplusStock));
            // TODO 由于是异步操作需要对秒杀中状态更新，之后需要写一个接口。
            tSeckillRecord.setStatus("成功");
            sendMessage.sendRecordSQLMessage(tSeckillRecord);
        }finally {
            lock.unlock();
        }
    }
}
