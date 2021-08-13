package com.glodon.groupsix.seckillprocess.rest;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class RestCommodityService {
    private final String ROOT_URL = "http://192.168.119.37:8080/commodity/";

    Set<String> sendRecordMap = new HashSet<>();


    /**
     * 调商品管理系统的接口，更新秒杀完结果
     * @param commodityCode 商品编号
     */
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


}
