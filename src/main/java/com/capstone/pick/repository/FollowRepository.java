package com.capstone.pick.repository;

import com.capstone.pick.domain.Follow;
import com.capstone.pick.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findByFromUserAndToUser(User fromUser, User toUser);
    List<Follow> findAllByFromUser(User fromUser); // 팔로잉 리스트
    List<Follow> findAllByToUser(User toUser); // 팔로워 리스트
}