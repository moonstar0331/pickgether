package com.capstone.pick.repository;

import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.constant.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    void deleteByIdAndUser_UserId(Long voteId, String userId);

    List<Vote> findByTitleContaining(String title);

    List<Vote> findByContentContaining(String content);

    List<Vote> findByUser_NicknameContaining(String nickname);
}
