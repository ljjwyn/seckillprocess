package com.glodon.groupsix.seckillprocess.controller;

import com.glodon.groupsix.seckillprocess.models.vi.SeckillInput;
import com.glodon.groupsix.seckillprocess.service.SeckillService;
import com.glodon.groupsix.seckillprocess.utils.Result;
import com.glodon.groupsix.seckillprocess.utils.Simulation;
import io.swagger.annotations.ApiParam;
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
        return seckillService.seckill("test", Simulation.getTel(),
                "小米MIX4", "4999");
    }

    @PostMapping(value = "/v1")
    public Result SeckillHandlerV1(@ApiParam(name="seckillInput", value="秒杀输入实体", required=true)
                                       @RequestBody SeckillInput seckillInput){
        return seckillService.seckill(seckillInput.getCommodityCode(), seckillInput.getPhone(),
                seckillInput.getCommodityName(), seckillInput.getSeckillPrice());
    }

    @GetMapping(value = "/v2")
    public Result SeckillHandlerV2Test(){
        return seckillService.seckillV2("test", Simulation.getTel(),
                "小米MIX4", "4999");
    }

    @PostMapping(value = "/v2")
    public Result SeckillHandlerV2(@ApiParam(name="seckillInput", value="秒杀输入实体", required=true)
                                       @RequestBody SeckillInput seckillInput){
        return seckillService.seckillV2(seckillInput.getCommodityCode(), seckillInput.getPhone(),
                seckillInput.getCommodityName(), seckillInput.getSeckillPrice());
    }

    @GetMapping(value = "/v3")
    public Result SeckillHandlerV3Test(){
        return seckillService.seckillV3("test", Simulation.getTel(),
                "小米MIX4", "4999");
    }

    @PostMapping(value = "/v3")
    public Result SeckillHandlerV3(@ApiParam(name="seckillInput", value="秒杀输入实体", required=true)
                                       @RequestBody SeckillInput seckillInput){
        return seckillService.seckillV3(seckillInput.getCommodityCode(), seckillInput.getPhone(),
                seckillInput.getCommodityName(), seckillInput.getSeckillPrice());
    }

    @GetMapping(value = "/v4")
    public Result SeckillHandlerV4Test(){
        return seckillService.seckillV4("test", Simulation.getTel(),
                "小米MIX4", "4999");
    }

    @PostMapping(value = "/v4")
    public Result SeckillHandlerV4(@ApiParam(name="seckillInput", value="秒杀输入实体", required=true)
                                       @RequestBody SeckillInput seckillInput){
        return seckillService.seckillV4(seckillInput.getCommodityCode(), seckillInput.getPhone(),
                seckillInput.getCommodityName(), seckillInput.getSeckillPrice());
    }

    @GetMapping(value = "/getCountAndUser")
    public Result GetCountAndUser(@ApiParam(name="commodityCode",
            value="商品编号",required=true) @RequestParam("commodityCode") String commodityCode){
        return seckillService.getCountAndUser(commodityCode);
    }

    @GetMapping("/recordByPhone")
    public Result recordByPhone(@ApiParam(name="phone", value="手机号", required=true)
                                                          @RequestParam("phone") String phone) {
        return seckillService.getRecordByPhone(phone);
    }

    @GetMapping("/recordByCommodityId")
    public Result recordByCommodityCode(@ApiParam(name="commodityCode",
            value="商品编号",required=true) @RequestParam("commodityCode") String commodityCode) {
        return seckillService.getRecordByCommodityId(commodityCode);
    }

}
