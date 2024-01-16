package com.gdscplantry.plantry.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public void opsForHashPut(String key, Map<String, Object> value, int expire_h) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll(key, value);
        redisTemplate.expire(key, expire_h, TimeUnit.HOURS);

        log.info("[Redis] Data saved successfully. -- " + key);
    }

    public String opsForHashGet(String key, Object hashKey) {
        return (String) redisTemplate.opsForHash().get(key, hashKey);
    }

    public void opsForValueSet(String key, String value, int expire_h) {
        ValueOperations<String, Object> stringValueOperations = redisTemplate.opsForValue();
        stringValueOperations.set(key, value);
        redisTemplate.expire(key, expire_h, TimeUnit.HOURS);

        log.info("[Redis] Data saved successfully. -- " + key);
    }

    public String opsForValueGet(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);

        log.info("[Redis] Data deleted successfully. -- " + key);
    }
}
