package com.glodon.groupsix.seckillprocess.controller;

import com.glodon.groupsix.seckillprocess.service.SeckillService;
import com.glodon.groupsix.seckillprocess.service.mq.SendMessage;
import com.glodon.groupsix.seckillprocess.utils.Result;
import com.glodon.groupsix.seckillprocess.utils.Simulation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping(value = "/seckill")
public class SeckillController {
    @Autowired
    SeckillService seckillService;



    @GetMapping(value = "/v1")
    public Result SeckillHandlerV1(){
        return seckillService.seckill("test", Simulation.getTel());
    }

    @GetMapping(value = "/v2")
    public Result SeckillHandlerV2(){
        return seckillService.seckillV2("test", Simulation.getTel());
    }

}
