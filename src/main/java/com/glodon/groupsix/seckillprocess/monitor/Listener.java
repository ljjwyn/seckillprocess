package com.glodon.groupsix.seckillprocess.monitor;


import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class Listener implements ApplicationListener<ContextRefreshedEvent>, Runnable {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Listener listener = event.getApplicationContext().getBean(Listener.class);
        new Thread(this).start();
    }

    @Override
    public void run() {}
}
