package com.capstone.pick.repository;

import com.capstone.pick.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByUserAndVote(User user, Vote vote);

    void deleteByUser(User user);

    Page<Bookmark> findAllByUser(User user, Pageable pageable);
    List<Bookmark> findAllByUser(User user);

    Bookmark findByUserAndVoteId(User user, Long voteId);
}
