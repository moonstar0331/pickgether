package com.capstone.pick.repository;

import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.constant.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    @EntityGraph(attributePaths = {"user"})
    void deleteByIdAndUser_UserId(Long voteId, String userId);

    @EntityGraph(attributePaths = {"user"})
    Page<Vote> findAllByCategory(Category category, Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    List<Vote> findByTitleContaining(String title);

    @EntityGraph(attributePaths = {"user"})
    List<Vote> findByContentContaining(String content);

    @EntityGraph(attributePaths = {"user"})
    Page<Vote> findAllByUser_UserId(String userId, Pageable pageable);
}
