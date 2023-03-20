package com.capstone.pick.service;

import com.capstone.pick.domain.Follow;
import com.capstone.pick.dto.FollowDto;
import com.capstone.pick.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;

    public void follow(FollowDto followDto) {
        followRepository.save(followDto.toEntity());
    }

    public void unfollow(String fromUserId, String toUserId) {
        Follow follow = followRepository.getReferenceById(
                new Follow.PK(fromUserId, toUserId)
        );
        followRepository.delete(follow);
    }

    public List<FollowDto> findFollowerList(String userId) {
        List<Follow> followerList = followRepository.findAllByToUserId(userId);
        return followerList.stream()
                .map(FollowDto::from)
                .collect(Collectors.toList());
    }

    public List<FollowDto> findFollowingList(String userId) {
        List<Follow> followingList = followRepository.findAllByFromUserId(userId);
        return followingList.stream()
                .map(FollowDto::from)
                .collect(Collectors.toList());
    }
}
