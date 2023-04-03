package com.capstone.pick.repository;

import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.VoteComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteCommentRepository extends JpaRepository<VoteComment, Long> {

    Page<VoteComment> findAllByVote(Vote vote, Pageable pageable);
}
