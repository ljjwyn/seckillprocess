package com.glodon.groupsix.seckillprocess.controller;

import com.glodon.groupsix.seckillprocess.models.vi.SeckillInput;
import com.glodon.groupsix.seckillprocess.service.SeckillService;
import com.glodon.groupsix.seckillprocess.utils.Result;
import com.glodon.groupsix.seckillprocess.utils.Simulation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(value = "/seckill")
public class SeckillController {
    @Autowired
    SeckillService seckillService;



    @GetMapping(value = "/v1")
    public Result SeckillHandlerV1Test(){
        return seckillService.seckill("test", Simulation.getTel());
    }

    @PostMapping(value = "/v1")
    public Result SeckillHandlerV1(@RequestBody SeckillInput seckillInput){
        return seckillService.seckill(seckillInput.getCommodityCode(), seckillInput.getPhone());
    }

    @GetMapping(value = "/v2")
    public Result SeckillHandlerV2(){
        return seckillService.seckillV2("test", Simulation.getTel(),
                "小米MIX4", "4999");
    }

    @PostMapping(value = "/v2")
    public Result SeckillHandlerV2(@RequestBody SeckillInput seckillInput){
        return seckillService.seckillV2(seckillInput.getCommodityCode(), seckillInput.getPhone(),
                seckillInput.getCommodityName(), seckillInput.getSeckillPrice());
    }

    @GetMapping(value = "/v3")
    public Result SeckillHandlerV3Test(){
        return seckillService.seckillV3("test", Simulation.getTel(),
                "小米MIX4", "4999");
    }

    @PostMapping(value = "/v3")
    public Result SeckillHandlerV3(@RequestBody SeckillInput seckillInput){
        return seckillService.seckillV3(seckillInput.getCommodityCode(), seckillInput.getPhone(),
                seckillInput.getCommodityName(), seckillInput.getSeckillPrice());
    }

}
