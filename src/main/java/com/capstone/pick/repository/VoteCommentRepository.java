package com.capstone.pick.repository;

import com.capstone.pick.domain.VoteComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteCommentRepository extends JpaRepository<VoteComment, Long> {
}
