package com.glodon.groupsix.seckillprocess.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class RestCommodityService {
    private final String ROOT_URL = "http://192.168.119.37:8080/commodity/";

    Set<String> sendRecordMap = new HashSet<>();


    /**
     * 调商品管理系统的接口，更新秒杀完结果,废弃
     * @param commodityCode 商品编号
     */
    @Deprecated
    public void sendCommoditySoldOutInfo(String commodityCode){
        if (sendRecordMap.contains(commodityCode)){
            return;
        }
        sendRecordMap.add(commodityCode);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> commodityCodeMap = new HashMap<>();
        commodityCodeMap.put("code", commodityCode);
        commodityCodeMap.put("surplusStock", 0);
        commodityCodeMap.put("status", 2);
        restTemplate.postForObject(ROOT_URL + "update", commodityCodeMap, Map.class);
    }

    /**
     * 调商品管理系统的接口，实时更新商品数据库的库存
     * @param commodityCode 商品编号
     */
    @Async
    public void sendCommodityStockCount(String commodityCode, int surplusStock){
        log.info("同步库存的处理线程：{}",Thread.currentThread().getName());
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> commodityCodeMap = new HashMap<>();
        commodityCodeMap.put("code", commodityCode);
        commodityCodeMap.put("surplusStock", surplusStock);
        restTemplate.postForObject(ROOT_URL + "reduceStock", commodityCodeMap, Map.class);
    }

    public static void main(String[] args) {
        RestCommodityService restCommodityService = new RestCommodityService();
        restCommodityService.sendCommodityStockCount("a1816",0);
    }


}
