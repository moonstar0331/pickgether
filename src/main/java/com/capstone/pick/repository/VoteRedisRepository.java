package com.capstone.pick.repository;

import com.capstone.pick.domain.constant.Category;
import com.capstone.pick.dto.VoteOptionCommentDto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteRedisRepository extends CrudRepository<VoteOptionCommentDto, Long> {

    List<VoteOptionCommentDto> findAllByCategory(Category category);
}
