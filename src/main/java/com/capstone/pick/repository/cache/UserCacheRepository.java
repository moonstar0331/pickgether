package com.capstone.pick.repository.cache;

import com.capstone.pick.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private final RedisTemplate<String, UserDto> redisTemplate;
    private HashOperations<String, String, String> hashOperations;
    private final ObjectMapper objectMapper;
    private final static Duration CACHE_TTL = Duration.ofDays(3);

    @PostConstruct
    public void init() {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void setUser(UserDto user) {
        try {
            hashOperations.put("USER", user.getUserId(), serializeUserDto(user));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    public Optional<UserDto> getUser(String username) throws JsonProcessingException {
        return Optional.ofNullable(deserializeUserDto(hashOperations.get("USER", username)));
    }

    public void update(UserDto userDto) {
        setUser(userDto);
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
