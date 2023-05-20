package com.capstone.pick.repository;

import com.capstone.pick.domain.Follow;
import com.capstone.pick.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    @EntityGraph(attributePaths = {"fromUser","toUser"})
    Follow findByFromUserAndToUser(User fromUser, User toUser);

    @EntityGraph(attributePaths = {"fromUser","toUser"})
    List<Follow> findAllByFromUser(User fromUser); // 팔로잉 리스트

    @EntityGraph(attributePaths = {"fromUser","toUser"})
    List<Follow> findAllByToUser(User toUser); // 팔로워 리스트
}