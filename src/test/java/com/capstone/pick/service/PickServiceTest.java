package com.capstone.pick.service;

import com.capstone.pick.domain.Pick;
import com.capstone.pick.domain.User;
import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.VoteOption;
import com.capstone.pick.exeption.DateExpiredException;
import com.capstone.pick.repository.PickRepository;
import com.capstone.pick.repository.UserRepository;
import com.capstone.pick.repository.VoteOptionRepository;
import com.capstone.pick.repository.cache.PickCacheRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@DisplayName("투표 참여 서비스 로직")
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = "com.capstone.pick.config")
@MockBean(JpaMetamodelMappingContext.class)
public class PickServiceTest {

    @InjectMocks
    private PickService pickService;

    @Mock
    private PickRepository pickRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private VoteOptionRepository voteOptionRepository;
    @Mock
    private PickCacheRepository pickCacheRepository;

    @DisplayName("투표 참여 - 정상 호출")
    @Test
    void pick() throws DateExpiredException {
        // given
        Long optionId = 1L;

        Vote vote = Vote.builder()
                .expiredAt(LocalDateTime.of(9999, 1, 1, 0, 0, 0))
                .build();
        VoteOption voteOption = VoteOption.builder().vote(vote).build();

        User user = createUser();
        Pick pick = Pick.builder()
                .id(1L)
                .user(user)
                .voteOption(voteOption)
                .build();

        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(mock(User.class)));
        given(voteOptionRepository.findById(optionId)).willReturn(Optional.of(voteOption));
        given(pickRepository.save(any(Pick.class))).willReturn(pick);

        // when
        pickService.pick(user.getUserId(), optionId);

        // then
        then(userRepository).should().findById(anyString());
        then(voteOptionRepository).should().findById(anyLong());
        verify(pickRepository, atLeastOnce()).save(any(Pick.class));
    }

    @DisplayName("투표 참여 - 날짜가 만료된 경우 - 에러 발생")
    @Test
    void pick_DateExpired() {
        // given
        String userId = "user";
        Long optionId = 1L;

        Vote vote = Vote.builder()
                .expiredAt(LocalDateTime.of(2000, 1, 1, 0, 0, 0))
                .build();
        VoteOption voteOption = VoteOption.builder().vote(vote).build();

        given(voteOptionRepository.findById(optionId)).willReturn(Optional.of(voteOption));

        // when & then
        Assertions.assertThrows(DateExpiredException.class, () -> pickService.pick(userId, optionId));
    }

    @DisplayName("투표 참여 - 참여한 유저가 존재하지 않는 경우 - 에러 발생")
    @Test
    void pick_NoUser() {
        // given
        String userId = "user";
        Long optionId = 1L;

        // when & then
        Assertions.assertThrows(EntityNotFoundException.class, () -> pickService.pick(userId, optionId));
    }

    @DisplayName("투표 참여 - 선택한 선택지가 존재하지 않는 경우 - 에러 발생")
    @Test
    void pick_NoVoteOption() {
        // given
        String userId = "user";
        Long optionId = 1L;

        // when
        when(voteOptionRepository.findById(optionId)).thenReturn(Optional.empty());

        // then
        Assertions.assertThrows(EntityNotFoundException.class, () -> pickService.pick(userId, optionId));
    }

    private static User createUser() {
        return User.builder()
                .userId("user")
                .userPassword("password")
                .email("email@email.com")
                .nickname("nick")
                .memo("memo")
                .birthday("0101")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
