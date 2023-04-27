package com.capstone.pick.service;

import com.capstone.pick.domain.Pick;
import com.capstone.pick.domain.User;
import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.VoteOption;
import com.capstone.pick.exeption.DateExpiredException;
import com.capstone.pick.repository.PickRepository;
import com.capstone.pick.repository.UserRepository;
import com.capstone.pick.repository.VoteOptionRepository;
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

@DisplayName("비즈니스 서비스 로직 - 투표 참여")
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = "com.capstone.pick.config")
@MockBean(JpaMetamodelMappingContext.class)
public class PickServiceTest {

    @InjectMocks private PickService pickService;

    @Mock private PickRepository pickRepository;
    @Mock private UserRepository userRepository;
    @Mock private VoteOptionRepository voteOptionRepository;

    @DisplayName("투표 참여 - 정상 호출")
    @Test
    void pick() throws DateExpiredException {
        String userId = "user";
        Long optionId = 1L;

        Vote vote = Vote.builder()
                .expiredAt(LocalDateTime.of(9999, 1, 1, 0, 0, 0))
                .build();
        VoteOption voteOption = VoteOption.builder().vote(vote).build();

        given(userRepository.findById(userId)).willReturn(Optional.of(mock(User.class)));
        given(voteOptionRepository.findById(optionId)).willReturn(Optional.of(voteOption));


        pickService.pick(userId, optionId);

        then(userRepository).should().findById(anyString());
        then(voteOptionRepository).should().findById(anyLong());
        verify(pickRepository, atLeastOnce()).save(any(Pick.class));
    }

    @DisplayName("투표 참여 - 날짜가 만료된 경우 - 에러 발생")
    @Test
    void pick_DateExpired() {
        String userId = "user";
        Long optionId = 1L;

        Vote vote = Vote.builder()
                .expiredAt(LocalDateTime.of(2000, 1, 1, 0, 0, 0))
                .build();
        VoteOption voteOption = VoteOption.builder().vote(vote).build();

        given(voteOptionRepository.findById(optionId)).willReturn(Optional.of(voteOption));

        Assertions.assertThrows(DateExpiredException.class, () -> pickService.pick(userId, optionId));
    }

    @DisplayName("투표 참여 - 참여한 유저가 존재하지 않는 경우 - 에러 발생")
    @Test
    void pick_NoUser() {
        String userId = "user";
        Long optionId = 1L;

        //when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> pickService.pick(userId, optionId));
    }

    @DisplayName("투표 참여 - 선택한 선택지가 존재하지 않는 경우 - 에러 발생")
    @Test
    void pick_NoVoteOption() {
        String userId = "user";
        Long optionId = 1L;

        //when(userRepository.findById(userId)).thenReturn(Optional.of(mock(User.class)));
        when(voteOptionRepository.findById(optionId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> pickService.pick(userId, optionId));
    }
}
