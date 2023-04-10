package com.capstone.pick.repository;

import com.capstone.pick.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final static Duration CACHE_TTL = Duration.ofDays(3);

    public void setUser(UserDto user) {
        try {
            redisTemplate.opsForValue().set(getKey(user.getUserId()), serializeUserDto(user), CACHE_TTL);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Optional<UserDto> getUser(String username) throws JsonProcessingException {
        return Optional.ofNullable(deserializeUserDto(redisTemplate.opsForValue().get(getKey(username))));
    }

    private String getKey(String username) {
        return "USER:" + username;
    }

    private String serializeUserDto(UserDto user) throws JsonProcessingException {
        return objectMapper.writeValueAsString(user);
    }

    private UserDto deserializeUserDto(String value) throws JsonProcessingException {
        if(value == null) return null;
        return objectMapper.readValue(value, UserDto.class);
    }
}
