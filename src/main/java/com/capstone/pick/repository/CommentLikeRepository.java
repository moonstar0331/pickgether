package com.capstone.pick.repository;

import com.capstone.pick.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Long countByVoteCommentId(Long voteCommentId);
    CommentLike findByVoteCommentIdAndUserUserId(Long voteCommentId, String userId);
    void deleteAllByVoteCommentId(Long voteCommentId);
}
