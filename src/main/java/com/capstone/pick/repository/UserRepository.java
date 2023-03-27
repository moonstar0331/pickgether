package com.capstone.pick.repository;

import com.capstone.pick.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    List<User> findByUserIdContaining(String userId);

    @Query("SELECT DISTINCT p.user FROM Pick p LEFT JOIN VoteOption vo ON vo.id = p.voteOption.id WHERE vo.vote.id = :voteId")
    List<User> findAllParticipants(@Param("voteId")Long voteId);
}
