package com.capstone.pick.repository;

import com.capstone.pick.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Follow.PK> {
    List<Follow> findAllByFromUserId(String userId); // 팔로잉 리스트
    List<Follow> findAllByToUserId(String userId); // 팔로워 리스트
}