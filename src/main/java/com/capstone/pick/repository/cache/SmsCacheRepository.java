package com.capstone.pick.repository.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SmsCacheRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final static Duration CACHE_TTL = Duration.ofDays(3);

    private final String PREFIX = "SMS:";

    public void setSmsCertification(String toPhoneNumber, String certNum) {
        redisTemplate.opsForValue().set(PREFIX + toPhoneNumber, certNum);
    }

    public String getSmsCertification(String toPhoneNumber) {
        return redisTemplate.opsForValue().get(PREFIX + toPhoneNumber);
    }

    public void deleteSmsCertification(String toPhoneNumber) {
        redisTemplate.delete(PREFIX + toPhoneNumber);
    }

    public boolean hasKey(String toPhoneNumber) {
        return redisTemplate.hasKey(PREFIX + toPhoneNumber);
    }
}
