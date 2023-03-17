package com.capstone.pick.service;

import com.capstone.pick.domain.*;
import com.capstone.pick.domain.constant.Category;
import com.capstone.pick.domain.constant.DisplayRange;
import com.capstone.pick.domain.constant.OrderCriteria;
import com.capstone.pick.dto.*;
import com.capstone.pick.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 서비스 로직 - 투표 댓글")
@ExtendWith(MockitoExtension.class)
public class VoteCommentServiceTest {

    @InjectMocks
    private VoteCommentService voteCommentService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private VoteRepository voteRepository;
    @Mock
    private VoteCommentRepository voteCommentRepository;
    @Mock
    private CommentLikeRepository commentLikeRepository;

    @DisplayName("댓글 상세 보기 페이지를 조회하면, 해당 투표 게시글에 대한 투표 댓글을 반환한다.")
    @Test
    void readComment() {
        // given
        User user1 = createUser("user1", "nick1");
        User user2 = createUser("user2", "nick2");
        Vote vote = createVote(1L, user1);

        VoteComment voteComment1 = createVoteComment(1L, user1, vote);
        VoteComment voteComment2 = createVoteComment(2L, user2, vote);

        List<VoteComment> voteComments = List.of(voteComment1, voteComment2);
        given(voteCommentRepository.getVoteCommentsByVoteId(vote.getId())).willReturn(voteComments);

        // when
        List<CommentDto> commentDtos = voteCommentService.readComment(vote.getId());

        // then
        assertThat(commentDtos.size()).isEqualTo(2);
        then(voteCommentRepository).should().getVoteCommentsByVoteId(vote.getId());
    }

    private static User createUser(String userId, String nickname) {
        return User.builder()
                .userId(userId)
                .userPassword("password")
                .email("email@email.com")
                .nickname(nickname)
                .memo("memo")
                .birthday(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private static Vote createVote(Long id, User user) {
        Vote vote = Vote.builder()
                .user(user)
                .title("title")
                .content("content")
                .category(Category.FREE)
                .createAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusDays(3))
                .isMultiPick(true)
                .displayRange(DisplayRange.PUBLIC)
                .build();
        ReflectionTestUtils.setField(vote, "id", id);
        return vote;
    }

    private static VoteComment createVoteComment(Long id, User user, Vote vote) {
        VoteComment voteComment = VoteComment.builder()
                .user(user)
                .vote(vote)
                .createAt(LocalDateTime.now())
                .content("content")
                .build();
        ReflectionTestUtils.setField(voteComment, "id", id);
        return voteComment;
    }
}
