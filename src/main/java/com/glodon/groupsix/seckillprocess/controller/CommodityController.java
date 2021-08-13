package com.glodon.groupsix.seckillprocess.controller;

import com.glodon.groupsix.seckillprocess.service.CommoditySyncService;
import com.glodon.groupsix.seckillprocess.utils.CodeMsg;
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

    @Autowired
    CommoditySyncService commoditySyncService;

    @PostMapping(value = "/putaway")
    public Result<String> letCommodityPutaway(@RequestBody Map<String, Object> requestParam){
        String commodityCode = (String) requestParam.get("code");
        Integer surplusStock = (Integer) requestParam.get("surplusStock");
        Integer startTime = (Integer) requestParam.get("startTimeMillSeconds");
        if (commodityCode == null || surplusStock == null){
            return new Result<>(CodeMsg.BIND_ERROR);
        }
        commoditySyncService.letCommodityPutaway(commodityCode, surplusStock.toString()
                , startTime==null?null:new Long(startTime));
        return new Result<>("更新上架信息");
    }

    @PostMapping(value = "/sold_out")
    public Result<String> letCommoditysoldOut(@RequestBody Map<String, Object> requestParam){
        String commodityCode = (String) requestParam.get("code");
        String phoneKey = commodityCode+"_phone";
        String startTime = commodityCode+"_startTime";
        log.info("商品管理系统请求：下架商品：{}",commodityCode);
        lettuceUtil.del(commodityCode);
        lettuceUtil.del(phoneKey);
        lettuceUtil.del(startTime);
        return new Result("更新下架信息");
    }
}
