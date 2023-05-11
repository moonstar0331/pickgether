package com.capstone.pick.repository.cache;

import com.capstone.pick.dto.PickCachingDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PickCacheRepository {

    private final RedisTemplate<String, PickCachingDto> redisTemplate;
    private final ObjectMapper objectMapper;

    public void setPick(PickCachingDto pick) {
        try {
            redisTemplate.opsForHash().put("PICK", pick.getVoteId(), serializePickDto(pick));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public PickCachingDto getPick(Long voteId) throws JsonProcessingException {
        return deserializePickDto((String) redisTemplate.opsForHash().get("PICK", voteId));
    }

    public Map<Object, Object> getAll() {
        return redisTemplate.opsForHash().entries("PICK");
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
