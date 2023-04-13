package com.capstone.pick.service;

import com.capstone.pick.dto.FollowDto;
import com.capstone.pick.repository.FollowRepository;
import com.capstone.pick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public void follow(FollowDto followDto) {
        followRepository.save(followDto.toEntity());
    }

    public void unfollow(String fromUser, String toUser) {
        followRepository.delete(followRepository.findByFromUserAndToUser(fromUser, toUser));
    }

    @Transactional(readOnly = true)
    public List<FollowDto> getFollowerList(String userId) {
        return userRepository.getReferenceById(userId).getFollowers().stream()
                .map(FollowDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FollowDto> getFollowingList(String userId) {
        return userRepository.getReferenceById(userId).getFollowing().stream()
                .map(FollowDto::from)
                .collect(Collectors.toList());
    }
}
