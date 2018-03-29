package com.yang.sell.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class RedisClock {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 加锁
     * @param key
     * @param value 当前时间+超时时间
     * @return
     * */
    public boolean lock(String key,String value) {
        //可以设置锁 true  否则false
        if (redisTemplate.opsForValue().setIfAbsent(key,value)) {
            return true;
        }

        String currentValue = redisTemplate.opsForValue().get(key);
        //如果锁过期 防止死锁
        if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue)<System.currentTimeMillis()) {
            //获取上一个锁的时间
            String oldValue = redisTemplate.opsForValue().getAndSet(key,value);
            //防止多线程进入，一个线程设置了新的时间另一个后设置，经次判断防止多线程扰乱
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
                return true;
            }
        }

        return false;
    }


    /**
     * 解锁
     * @param key
     * @param value
     * */
    public void unlock(String key,String value) {
        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            log.error("【redis分布式锁】解锁异常,{}",e);
        }
    }
}
