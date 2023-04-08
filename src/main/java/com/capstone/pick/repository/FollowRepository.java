package com.capstone.pick.repository;

import com.capstone.pick.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findByFromUserAndToUser(String fromUser, String toUser);
    List<Follow> findAllByFromUser(String fromUser); // 팔로잉 리스트
    List<Follow> findAllByToUser(String toUser); // 팔로워 리스트
}