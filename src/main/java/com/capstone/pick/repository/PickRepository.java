package com.capstone.pick.repository;

import com.capstone.pick.domain.Pick;
import com.capstone.pick.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PickRepository extends JpaRepository<Pick, Long> {
    @Query("SELECT p.user FROM Pick p LEFT JOIN VoteOption vo ON vo.id = p.voteOption.id WHERE vo.vote.id = :voteId")
    Page<User> findAllByVoteId(@Param("voteId")Long voteId, Pageable pageable);

    List<Pick> findAllByVoteOptionId(Long voteOptionId);

    @Query("SELECT count(p) FROM Pick p WHERE p.voteOption.id = :optionId")
    Long getCountListByOptionId(@Param("optionId")Long optionId);
}
