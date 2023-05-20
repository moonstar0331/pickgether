package com.capstone.pick.repository;

import com.capstone.pick.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    @EntityGraph(attributePaths = {"user","vote"})
    Optional<Bookmark> findByUserAndVote(User user, Vote vote);

    @EntityGraph(attributePaths = {"user","vote"})
    void deleteByUser(User user);

    @EntityGraph(attributePaths = {"user","vote"})
    Page<Bookmark> findAllByUser(User user, Pageable pageable);

    @EntityGraph(attributePaths = {"user","vote"})
    List<Bookmark> findAllByUser(User user);

    @EntityGraph(attributePaths = {"user","vote"})
    Bookmark findByUserAndVoteId(User user, Long voteId);
}
