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

    public void unfollow(String fromUser, String toUser) {
        followRepository.delete(followRepository.findByFromUserAndToUser(fromUser, toUser));
    }

    public List<FollowDto> findFollowerList(String toUser) {
        List<Follow> followerList = followRepository.findAllByToUser(toUser);
        return followerList.stream()
                .map(FollowDto::from)
                .collect(Collectors.toList());
    }

    public List<FollowDto> findFollowingList(String fromUser) {
        List<Follow> followingList = followRepository.findAllByFromUser(fromUser);
        return followingList.stream()
                .map(FollowDto::from)
                .collect(Collectors.toList());
    }
}
