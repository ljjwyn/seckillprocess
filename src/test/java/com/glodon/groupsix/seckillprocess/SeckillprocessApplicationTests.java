package com.glodon.groupsix.seckillprocess;

import com.glodon.groupsix.seckillprocess.mapper.TSeckillRecordDao;
import com.glodon.groupsix.seckillprocess.service.SeckillService;
import com.glodon.groupsix.seckillprocess.service.mq.SendMessage;
import com.glodon.groupsix.seckillprocess.utils.JedisUtil;
import com.glodon.groupsix.seckillprocess.utils.LettuceUtil;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
class SeckillprocessApplicationTests {

    @Autowired
    JedisUtil jedisUtil;

    @Autowired
    SeckillService seckillService;

    @Autowired
    TSeckillRecordDao tSeckillRecordDao;

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

    }

    /**
     * 10个线程 执行100次
     * invocations:调用次数，执行次数与线程无关
     * threads:线程
     */
    @Test
    @PerfTest(threads = 200)
    void test() {
        System.out.println();
    }

    @Autowired
    SendMessage sendMessage;

    @Test
    void mqTest(){
        //seckillService.getRecordByPhone("15964969802");
        tSeckillRecordDao.selectByPrimaryKey(1);
    }

    @Autowired
    LettuceUtil lettuceUtil;

    @Test
    void lettuceUtilTest(){
        lettuceUtil.set("test","100");
    }


}
