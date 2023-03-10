package com.capstone.pick.repository;

import com.capstone.pick.domain.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteOptionRepository extends JpaRepository<VoteOption, Long> {
    List<VoteOption> findAllByVoteId(Long vote_id);
}
