package com.glodon.groupsix.seckillprocess.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectRecordConfig {

    //队列 起名：RecordDirectQueue
    @Bean
    public Queue RecordDirectQueue() {
        return new Queue("RecordDirectQueue",true);
    }

    //Direct交换机 起名：RecordDirectExchange
    @Bean
    DirectExchange RecordDirectExchange() {
        //  return new DirectExchange("TestDirectExchange",true,true);
        return new DirectExchange("RecordDirectExchange",true,false);
    }

    //绑定  将队列和交换机绑定, 并设置用于匹配键：RecordDirectRouting
    @Bean
    Binding bindingDirectRecord() {
        return BindingBuilder.bind(RecordDirectQueue()).to(RecordDirectExchange()).with("RecordDirectRouting");
    }

    @Bean
    DirectExchange SQLDirectExchange() {
        return new DirectExchange("SQLDirectExchange");
    }
}
