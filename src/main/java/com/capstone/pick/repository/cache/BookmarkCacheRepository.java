package com.capstone.pick.repository.cache;

import com.capstone.pick.dto.BookmarkDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookmarkCacheRepository {

    private final RedisTemplate<String, BookmarkDto> redisTemplate;
    private final ObjectMapper objectMapper;
    private final static Duration CACHE_TTL = Duration.ofDays(3);

    public void setBookmark(BookmarkDto bookmark) {
        try {
            redisTemplate.opsForHash().put("BOOKMARK", bookmark.getVoteDto().getId(), serializeBookmark(bookmark));
            redisTemplate.expire("BOOKMARK", 3, TimeUnit.DAYS);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public BookmarkDto getBookmark(Long voteId) throws JsonProcessingException {
        return deserializeBookmark((String) redisTemplate.opsForHash().get("BOOKMARK", voteId));
    }

    public Map<Object, Object> getAll() {
        return redisTemplate.opsForHash().entries("BOOKMARK");
    }

    public void delete(Long voteId) {
        redisTemplate.opsForHash().delete("BOOKMARK", voteId);
    }

    private String serializeBookmark(BookmarkDto bookmark) throws JsonProcessingException {
        return objectMapper.writeValueAsString(bookmark);
    }

    private BookmarkDto deserializeBookmark(String value) throws JsonProcessingException {
        if(value == null) return null;
        return objectMapper.readValue(value, BookmarkDto.class);
    }
}
