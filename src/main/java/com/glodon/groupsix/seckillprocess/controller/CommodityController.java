package com.glodon.groupsix.seckillprocess.controller;

import com.glodon.groupsix.seckillprocess.utils.LettuceUtil;
import com.glodon.groupsix.seckillprocess.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping(value = "/commodity")
public class CommodityController {

    @Autowired
    LettuceUtil lettuceUtil;

    @PostMapping(value = "/putaway")
    public Result<String> letCommodityPutaway(@RequestBody Map<String, Object> requestParam){
        String commodityCode = (String) requestParam.get("code");
        String surplusStock = String.valueOf(requestParam.get("surplusStock"));
        log.info("商品管理系统请求：上架商品：{}，库存为：{}",commodityCode, surplusStock);
        lettuceUtil.set(commodityCode, surplusStock);
        return new Result("更新上架信息");
    }

    @PostMapping(value = "/sold_out")
    public Result<String> letCommoditysoldOut(@RequestBody Map<String, Object> requestParam){
        String commodityCode = (String) requestParam.get("code");
        String phoneKey = commodityCode+"_phone";
        log.info("商品管理系统请求：下架商品：{}",commodityCode);
        lettuceUtil.del(commodityCode);
        lettuceUtil.del(phoneKey);
        return new Result("更新下架信息");
    }
}
