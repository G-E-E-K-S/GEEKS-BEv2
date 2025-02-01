package com.my_geeks.geeks.mail;

import com.my_geeks.geeks.exception.CustomException;
import com.my_geeks.geeks.exception.ErrorCode;
import lombok.Getter;
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
