package com.capstone.pick.repository.cache;

import com.capstone.pick.dto.PickCachingDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PickCacheRepository {

    private final RedisTemplate<String, PickCachingDto> redisTemplate;
    private HashOperations<String, Long, String> hashOperations;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void setPick(PickCachingDto pick) {
        try {
            hashOperations.put("PICK", pick.getVoteId(), serializePickDto(pick));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public PickCachingDto getPick(Long voteId) throws JsonProcessingException {
        return deserializePickDto((String) redisTemplate.opsForHash().get("PICK", voteId));
    }

    public Map<Long, PickCachingDto> getAll() {

        try {
            Map<Long, PickCachingDto> map = new HashMap<>();
            for(Long key : hashOperations.entries("PICK").keySet()) {
                map.put(key, deserializePickDto(hashOperations.get("PICK", key)));
            }
            return map;
        } catch (Exception e) {
            log.error(e.getMessage());
            return Map.of();
        }
    }

    private String getKey(Long voteId) {
        return "PICK:" + voteId;
    }

    private String serializePickDto(PickCachingDto pick) throws JsonProcessingException {
        return objectMapper.writeValueAsString(pick);
    }

    private PickCachingDto deserializePickDto(String value) throws JsonProcessingException {
        if(value == null) return null;
        return objectMapper.readValue(value, PickCachingDto.class);
    }
}
