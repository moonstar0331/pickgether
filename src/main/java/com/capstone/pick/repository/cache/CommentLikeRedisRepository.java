package com.capstone.pick.repository.cache;

import com.capstone.pick.dto.CommentLikeDto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentLikeRedisRepository extends CrudRepository<CommentLikeDto, Long> {
    List<CommentLikeDto> findAll();
}
