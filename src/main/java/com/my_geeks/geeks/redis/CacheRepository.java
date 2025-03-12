package com.my_geeks.geeks.redis;

import com.my_geeks.geeks.domain.user.entity.User;
import com.my_geeks.geeks.domain.user.repository.UserRepository;
import com.my_geeks.geeks.exception.CustomException;
import com.my_geeks.geeks.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheRepository {
    private final UserRepository userRepository;

    @Cacheable(value = "UserCache", key = "#userId", cacheManager = "cacheManager")
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @CacheEvict(value = "UserCache", key = "#userId", cacheManager = "cacheManager")
    public void evictUser(Long userId) {
        System.out.println("evict");
    }
}
