package com.capstone.pick.repository;

import com.capstone.pick.domain.CommentLike;
import com.capstone.pick.domain.User;
import com.capstone.pick.domain.VoteComment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    @EntityGraph(attributePaths = {"user","voteComment"})
    Long countByVoteCommentId(Long voteCommentId);

    @EntityGraph(attributePaths = {"user","voteComment"})
    CommentLike findByVoteCommentIdAndUserUserId(Long voteCommentId, String userId);

    @EntityGraph(attributePaths = {"user","voteComment"})
    void deleteAllByVoteCommentId(Long voteCommentId);

    @EntityGraph(attributePaths = {"user","voteComment"})
    Optional<CommentLike> findByUserAndVoteComment(User user, VoteComment voteComment);

    @EntityGraph(attributePaths = {"user","voteComment"})
    List<CommentLike> findAllByUser_UserId(String userId);
}
