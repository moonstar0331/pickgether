package com.capstone.pick.service;

import com.capstone.pick.domain.Follow;
import com.capstone.pick.domain.User;
import com.capstone.pick.dto.FollowDto;
import com.capstone.pick.repository.FollowRepository;
import com.capstone.pick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    /**
     * @brief  다른 사용자를 팔로우 한다
     * @param  followDto 나와 내가 팔로우 하려는 사람의 정보
     */
    public void follow(FollowDto followDto) {

        Follow follow = followDto.toEntity();
        User fromUser = follow.getFromUser(); // 내 정보
        User toUser = follow.getToUser(); // 팔로우 하려고 하는 사람의 정보

        Follow myFollowInfo = followRepository.findByFromUserAndToUser(fromUser, toUser);
        Follow friendFollowInfo = followRepository.findByFromUserAndToUser( toUser,fromUser);

        if (friendFollowInfo == null){ // 친구가 나를 팔로우 하지 않았다면
            followRepository.save(myFollowInfo); // 내 팔로우 정보만 저장

        }else{ // 맞팔상태 라면
            myFollowInfo.beFriends(); // 서로 친구임을 표시하도록 상태정보 업데이트
            friendFollowInfo.beFriends();
            followRepository.save(myFollowInfo);
            followRepository.save(friendFollowInfo);
        }

    }

    /**
     * @brief  다른 사용자를 언팔로우 한다
     * @param  fromUser 내 정보
     * @param  toUser 내가 언팔로우 하려는 사람의 정보
     */
    public void unfollow(User fromUser, User toUser) {

        Follow myFollowInfo = followRepository.findByFromUserAndToUser(fromUser, toUser); // 내 정보
        Follow friendFollowInfo = followRepository.findByFromUserAndToUser( toUser,fromUser); // 내가 언팔로우 하려는 사람의 정보

        if (friendFollowInfo == null){ // 친구가 나를 팔로우 하지 않았다면
            followRepository.delete(myFollowInfo); // 내 팔로우 정보만 삭제
        }else{ // 맞팔상태 라면
            followRepository.delete(myFollowInfo); // 내 정보는 삭제

            friendFollowInfo.notFriends(); // 친구 또한 서로 친구가 아님을 표시하도록 상태정보 업데이트
            followRepository.save(friendFollowInfo);
        }
    }

    @Transactional(readOnly = true)
    public List<FollowDto> getFollowerList(String userId) {
        List<Follow> followerList = followRepository.findAllByToUser(userRepository.getReferenceById(userId));
        if(followerList == null){
            return new ArrayList<FollowDto>();
        }
        return followerList.stream()
                .map(FollowDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FollowDto> getFollowingList(String userId) {
        List<Follow> followingList = followRepository.findAllByFromUser(userRepository.getReferenceById(userId));
        return followingList.stream()
                .map(FollowDto::from)
                .collect(Collectors.toList());
    }
}
