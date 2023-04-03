package com.capstone.pick.repository;

import com.capstone.pick.domain.CommentLike;
import com.capstone.pick.domain.User;
import com.capstone.pick.domain.VoteComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Long countByVoteCommentId(Long voteCommentId);
    CommentLike findByVoteCommentIdAndUserUserId(Long voteCommentId, String userId);
    void deleteAllByVoteCommentId(Long voteCommentId);

    Optional<CommentLike> findByUserAndVoteComment(User user, VoteComment voteComment);
}
