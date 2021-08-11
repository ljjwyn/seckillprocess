package com.glodon.groupsix.seckillprocess.controller;

import com.glodon.groupsix.seckillprocess.service.SeckillService;
import com.glodon.groupsix.seckillprocess.utils.Result;
import com.glodon.groupsix.seckillprocess.utils.Simulation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(value = "/v3")
    public Result SeckillHandlerV3(){
        return seckillService.seckillV3("test", Simulation.getTel());
    }

}
