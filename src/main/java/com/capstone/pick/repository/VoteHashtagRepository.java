package com.capstone.pick.repository;

import com.capstone.pick.domain.VoteHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteHashtagRepository  extends JpaRepository<VoteHashtag, Long> {
    List<VoteHashtag> findAllByVoteId(Long voteId);
    void deleteAllByVoteId(Long voteId);
}
