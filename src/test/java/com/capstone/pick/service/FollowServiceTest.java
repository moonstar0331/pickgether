package com.capstone.pick.service;

import com.capstone.pick.domain.Follow;
import com.capstone.pick.domain.User;
import com.capstone.pick.dto.FollowDto;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.repository.FollowRepository;
import com.capstone.pick.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.when;

@DisplayName("팔로우 서비스 로직")
@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {

    @InjectMocks
    private FollowService followService;

    @Mock
    private FollowRepository followRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("친구가 나를 팔로우 하고 있을 때 내가 친구를 팔로우")
    void 맞팔로우() {
        // given
        User fromUser = User.builder().userId("fromUser").build();
        User toUser = User.builder().userId("toUser").build();

        FollowDto followDto = FollowDto.builder()
                .fromUser(UserDto.from(fromUser))
                .toUser(UserDto.from(toUser))
                .build();

        Follow myFollowInfo = createFollow(1L, fromUser, toUser, false);
        Follow friendFollowInfo = createFollow(2L, toUser, fromUser, false);

        given(followRepository.findByFromUserAndToUser(fromUser, toUser)).willReturn(myFollowInfo);
        given(followRepository.findByFromUserAndToUser(toUser, fromUser)).willReturn(friendFollowInfo);

        // when
        followService.follow(followDto);

        // then
        assertThat(myFollowInfo.isFriend()).isEqualTo(true);
        assertThat(friendFollowInfo.isFriend()).isEqualTo(true);
    }

    @Test
    @DisplayName("인플루언서를 팔로우")
    void 팔로우() {
        // given
        User fromUser = User.builder().userId("fromUser").build();
        User toUser = User.builder().userId("toUser").build();

        FollowDto followDto = FollowDto.builder()
                .fromUser(UserDto.from(fromUser))
                .toUser(UserDto.from(toUser))
                .build();

        Follow myFollowInfo = createFollow(1L, fromUser, toUser, false);
        Follow friendFollowInfo = null;

        given(followRepository.findByFromUserAndToUser(fromUser, toUser)).willReturn(myFollowInfo);
        given(followRepository.findByFromUserAndToUser(toUser, fromUser)).willReturn(friendFollowInfo);

        // when
        followService.follow(followDto);

        // then
        then(followRepository).should(times(1)).save(myFollowInfo);
        assertThat(myFollowInfo.isFriend()).isEqualTo(false);
    }

    @Test
    @DisplayName("친구가 나를 팔로우 하고 있을 때 내가 친구를 언팔로우")
    void 친구를언팔로우() {
        // given
        User fromUser = User.builder().userId("fromUser").build();
        User toUser = User.builder().userId("toUser").build();

        Follow myFollowInfo = createFollow(1L, fromUser, toUser, true);
        Follow friendFollowInfo = createFollow(2L, toUser, fromUser, true);

        given(followRepository.findByFromUserAndToUser(fromUser, toUser)).willReturn(myFollowInfo);
        given(followRepository.findByFromUserAndToUser(toUser, fromUser)).willReturn(friendFollowInfo);

        // when
        followService.unfollow(fromUser, toUser);

        // then
        then(followRepository).should(times(1)).delete(myFollowInfo);
        assertThat(friendFollowInfo.isFriend()).isEqualTo(false);
    }

    @Test
    @DisplayName("인플루언서를 언팔로우")
    void 인플루언서를언팔로우() {
        // given
        User fromUser = User.builder().userId("fromUser").build();
        User toUser = User.builder().userId("toUser").build();

        Follow myFollowInfo = createFollow(1L, fromUser, toUser, true);
        Follow friendFollowInfo = null;

        given(followRepository.findByFromUserAndToUser(fromUser, toUser)).willReturn(myFollowInfo);
        given(followRepository.findByFromUserAndToUser(toUser, fromUser)).willReturn(friendFollowInfo);

        // when
        followService.unfollow(fromUser, toUser);

        // then
        then(followRepository).should(times(1)).delete(myFollowInfo);
    }

    @Test
    @DisplayName("팔로워 리스트 조회 - 해당 유저의 팔로워 리스트 반환")
    void findFollowerList() {
        // given
        User fromUser1 = User.builder().userId("fromUser1").build();
        User fromUser2 = User.builder().userId("fromUser2").build();
        User user = User.builder().userId("user").build();
        Follow follow1 = createFollow(1L, fromUser1, user, false);
        Follow follow2 = createFollow(2L, fromUser2, user, false);

        List<Follow> followerList = new ArrayList<>();
        followerList.add(follow1);
        followerList.add(follow2);

        when(userRepository.getReferenceById(user.getUserId())).thenReturn(user);
        when(followRepository.findAllByToUser(any(User.class))).thenReturn(followerList);

        // when
        List<FollowDto> followerDtos = followService.getFollowerList("user");

        // then
        assertThat(followerDtos.get(0))
                .hasFieldOrPropertyWithValue("id", follow1.getId());
        assertThat(followerDtos.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("팔로잉 리스트 조회 - 해당 유저의 팔로잉 리스트 반환")
    void findFollowingList() {
        // given
        User toUser1 = User.builder().userId("toUser1").build();
        User toUser2 = User.builder().userId("toUser2").build();
        User user = User.builder().userId("user").build();
        Follow follow1 = createFollow(1L, user, toUser1, false);
        Follow follow2 = createFollow(2L, user, toUser2, false);

        List<Follow> followingList = new ArrayList<>();
        followingList.add(follow1);
        followingList.add(follow2);

        when(userRepository.getReferenceById(user.getUserId())).thenReturn(user);
        when(followRepository.findAllByFromUser(any(User.class))).thenReturn(followingList);

        // when
        List<FollowDto> followerDtos = followService.getFollowingList("user");

        // then
        assertThat(followerDtos.get(0))
                .hasFieldOrPropertyWithValue("id", follow1.getId());
        assertThat(followerDtos.size()).isEqualTo(2);
    }

    private static Follow createFollow(Long id, User fromUser, User toUser, boolean isFriend) {
        return Follow.builder()
                .id(id)
                .fromUser(fromUser)
                .toUser(toUser)
                .isFriend(isFriend)
                .build();
    }
}