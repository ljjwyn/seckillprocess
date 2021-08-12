package com.glodon.groupsix.seckillprocess.service.mq;

import com.glodon.groupsix.seckillprocess.models.vo.TSeckillRecord;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendMessage {
    //使用RabbitTemplate,这提供了接收/发送等等方法
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendSeckillMessage(TSeckillRecord tSeckillRecord){
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", tSeckillRecord);
    }

    public void sendRecordSQLMessage(TSeckillRecord tSeckillRecord){
        //将消息携带绑定键值：TestDirectRouting 发送到交换机RecordDirectExchange
        rabbitTemplate.convertAndSend("RecordDirectExchange", "RecordDirectRouting", tSeckillRecord);
    }
}
