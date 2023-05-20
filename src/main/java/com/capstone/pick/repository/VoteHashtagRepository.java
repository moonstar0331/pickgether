package com.capstone.pick.repository;

import com.capstone.pick.domain.VoteHashtag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteHashtagRepository  extends JpaRepository<VoteHashtag, Long> {
    @EntityGraph(attributePaths = {"hashtag","vote"})
    List<VoteHashtag> findAllByVoteId(Long voteId);

    @EntityGraph(attributePaths = {"hashtag","vote"})
    void deleteAllByVoteId(Long voteId);

    @EntityGraph(attributePaths = {"hashtag","vote"})
    List<VoteHashtag> findByHashtag_ContentContaining(String content);
}
