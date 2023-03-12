package com.capstone.pick.repository;

import com.capstone.pick.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    void deleteByIdAndUser_UserId(Long voteId, String userId);
}
