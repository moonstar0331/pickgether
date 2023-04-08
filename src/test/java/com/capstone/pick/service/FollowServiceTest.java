package com.capstone.pick.service;

import com.capstone.pick.domain.Follow;
import com.capstone.pick.dto.FollowDto;
import com.capstone.pick.repository.FollowRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("팔로우")
@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {

    @InjectMocks
    private FollowService followService;
    @Mock
    private FollowRepository followRepository;

    @Test
    @DisplayName("유저 아이디를 넘겨주면, 해당 유저의 팔로워 리스트를 받아올 수 있다.")
    void findFollowerList() {
        //given
        Follow follow1 = createFollow("from_id1", "to_id");
        Follow follow2 = createFollow("from_id2", "to_id");
        Follow follow3 = createFollow("from_id3", "to_id");

        List<Follow> followerList = new ArrayList<>();
        followerList.add(follow1);
        followerList.add(follow2);
        followerList.add(follow3);

        given(followRepository.findAllByToUserId("to_id")).willReturn(followerList);

        //when
        List<FollowDto> followerDtos = followService.findFollowerList("to_id");

        //then
        assertThat(followerDtos.get(0))
                .hasFieldOrPropertyWithValue("fromUserId", follow1.getFromUserId())
                .hasFieldOrPropertyWithValue("toUserId", follow1.getToUserId());
        assertThat(followerDtos.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("유저 아이디를 넘겨주면, 해당 유저의 팔로잉 리스트를 받아올 수 있다.")
    void findFollowingList() {
        //given
        Follow follow1 = createFollow("from_id", "to_id1");
        Follow follow2 = createFollow("from_id", "to_id2");
        Follow follow3 = createFollow("from_id", "to_id3");

        List<Follow> followingList = new ArrayList<>();
        followingList.add(follow1);
        followingList.add(follow2);
        followingList.add(follow3);

        given(followRepository.findAllByFromUserId("from_id")).willReturn(followingList);

        //when
        List<FollowDto> followingDtos = followService.findFollowingList("from_id");

        //then
        assertThat(followingDtos.get(0))
                .hasFieldOrPropertyWithValue("fromUserId", follow1.getFromUserId())
                .hasFieldOrPropertyWithValue("toUserId", follow1.getToUserId());
        assertThat(followingDtos.size()).isEqualTo(3);
    }

    private static Follow createFollow(String fromUser, String toUser) {
        return Follow.builder()
                .fromUserId(fromUser)
                .toUserId(toUser)
                .build();
    }

}