package com.my_geeks.geeks.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, String> redisTemplate;

    public String getCode(String key) {
        String code = null;
        code = redisTemplate.opsForValue().get(key);
        return code;
    }

    public void saveCode(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, 180, TimeUnit.SECONDS);
    }
}
