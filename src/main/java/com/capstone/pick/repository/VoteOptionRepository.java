package com.capstone.pick.repository;

import com.capstone.pick.domain.VoteOption;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteOptionRepository extends JpaRepository<VoteOption, Long> {
    @EntityGraph(attributePaths = {"vote"})
    List<VoteOption> findAllByVoteId(Long voteId);
}
