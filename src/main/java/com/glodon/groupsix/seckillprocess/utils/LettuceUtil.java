package com.glodon.groupsix.seckillprocess.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author ljjwyn
 */
@Component
public class LettuceUtil {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 新增一个  add
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取key对应的value
     *
     * @param key
     * @return
     */
    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }


    /**
     * 新增一个  sadd
     *
     * @param key
     * @param value
     */
    public void sadd(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 删除集合中的值  srem
     *
     * @param key
     * @param value
     */
    public void remove(String key, String value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    /**
     * 获取集合中所有的值 smembers
     *
     * @param key
     * @return
     */
    public Set<String> values(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 判断是否包含  sismember
     *
     * @param key
     * @param value
     */
    public Boolean contains(String key, String value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }
}