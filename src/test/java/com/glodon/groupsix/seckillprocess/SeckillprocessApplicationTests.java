package com.glodon.groupsix.seckillprocess;

import com.glodon.groupsix.seckillprocess.service.SeckillService;
import com.glodon.groupsix.seckillprocess.service.mq.SendMessage;
import com.glodon.groupsix.seckillprocess.utils.JedisUtil;
import com.glodon.groupsix.seckillprocess.utils.LettuceUtil;
import com.glodon.groupsix.seckillprocess.utils.Simulation;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


@RunWith(SpringRunner.class)
@SpringBootTest
class SeckillprocessApplicationTests {

    @Autowired
    JedisUtil jedisUtil;

    @Autowired
    SeckillService seckillService;

    /**
     * 引入 ContiPerf 进行性能测试
     * 激活性能测试，否则@PerfTest 无法生效
     */
    @Rule
    public ContiPerfRule contiPerfRule = new ContiPerfRule();

    @Test
    void redisTest() {
        jedisUtil.set("test", "100");
    }

    @Test
    void test2()throws Exception{
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(200);
        final CountDownLatch countDownLatch = new CountDownLatch(500);
        long l = System.currentTimeMillis();
        for (int i = 0; i < 200; i++) {
            final int count = i;
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    System.out.println("当前线程:"+Thread.currentThread().getName()+"抢购结果"+seckillService.seckill("test", Simulation.getTel()));
                    semaphore.release();
                } catch (Exception e) {
                    // log.error("exception" , e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        long a = System.currentTimeMillis();
        System.out.println(a-l);

        executorService.shutdown();

        //log.info("size:{}" , map.size());
    }

    /**
     * 10个线程 执行100次
     * invocations:调用次数，执行次数与线程无关
     * threads:线程
     */
    @Test
    @PerfTest(threads = 200)
    void test() {
        System.out.println("当前线程:"+Thread.currentThread().getName()+"抢购结果"+seckillService.seckill("test", Simulation.getTel()));
    }

    @Autowired
    SendMessage sendMessage;

    @Test
    void mqTest(){
        sendMessage.sendSeckillMessage("test","15964969802");
    }

    @Autowired
    LettuceUtil lettuceUtil;

    @Test
    void lettuceUtilTest(){
        lettuceUtil.set("test","100");
    }


}
