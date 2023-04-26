package com.capstone.pick.repository;

import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.constant.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    void deleteByIdAndUser_UserId(Long voteId, String userId);

    Page<Vote> findAllByCategory(Category category, Pageable pageable);

    List<Vote> findByTitleContaining(String title);

    List<Vote> findByContentContaining(String content);

    Page<Vote> findAllByUser_UserId(String userId, Pageable pageable);
}
