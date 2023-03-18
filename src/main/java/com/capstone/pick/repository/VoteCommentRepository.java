package com.capstone.pick.repository;

import com.capstone.pick.domain.VoteComment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteCommentRepository extends JpaRepository<VoteComment, Long> {
    List<VoteComment> findAllByVoteId(Long voteId);
    List<VoteComment> findAllByVoteId(Long voteId, Sort sort);

    @Query("SELECT vc FROM VoteComment vc LEFT JOIN CommentLike cl ON vc.id = cl.voteComment.id WHERE vc.vote.id = :voteId GROUP BY vc.id ORDER BY COUNT(cl.id) DESC")
    List<VoteComment> findAllByVoteIdOrderByLikeDesc(@Param("voteId") Long voteId);

}
