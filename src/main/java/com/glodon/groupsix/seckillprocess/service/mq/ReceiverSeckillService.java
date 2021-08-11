package com.glodon.groupsix.seckillprocess.service.mq;

import com.glodon.groupsix.seckillprocess.utils.LettuceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
@RabbitListener(queues = "TestDirectQueue")//监听的队列名称 TestDirectQueue
public class ReceiverSeckillService {
    @Autowired
    LettuceUtil lettuceUtil;

    private static ReentrantLock lock = new ReentrantLock();

    @RabbitHandler
    public void process(Map testMessage) {
        // 高并发测试记得注释掉这些日志
        log.info("收到用户抢购消息  :{} ", testMessage.toString());
        String key = (String) testMessage.get("key");
        String value = (String) testMessage.get("value");
        String createTime = (String) testMessage.get("createTime");
        String phoneKey = key+"_phone";
        StringBuilder storePhoneNumber = new StringBuilder();
        storePhoneNumber.append(value);
        storePhoneNumber.append("_");
        storePhoneNumber.append(createTime);
        asyncRedisProcess(key, value, phoneKey, storePhoneNumber.toString());
    }

    public void asyncRedisProcess(String key, String value, String phoneKey, String storePhoneNumber){
        //TODO 这里写个持久化日志。
        if (lettuceUtil.contains(phoneKey, value)){
            log.info("失败！重复抢购");
            return ;
        }
        int surplusStock = Integer.parseInt(lettuceUtil.get(key));
        if (surplusStock == 0){
            log.info("失败！抢完了，库存为0");
            return ;
        }
        // TODO 还是上锁，想想解决方案。
        lock.lock();
        try {
            surplusStock-=1;
            lettuceUtil.sadd(phoneKey, storePhoneNumber);
            lettuceUtil.set(key, String.valueOf(surplusStock));
            // TODO 由于是异步操作需要对秒杀中状态更新，之后需要写一个接口。
        }finally {
            lock.unlock();
        }
    }
}
