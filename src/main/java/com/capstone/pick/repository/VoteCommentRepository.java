package com.capstone.pick.repository;

import com.capstone.pick.domain.VoteComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteCommentRepository extends JpaRepository<VoteComment, Long> {
    List<VoteComment> getVoteCommentsByVoteId(Long voteId);
}
