package com.capstone.pick.service;

import com.capstone.pick.domain.CommentLike;
import com.capstone.pick.domain.User;
import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.VoteComment;
import com.capstone.pick.domain.constant.Category;
import com.capstone.pick.domain.constant.DisplayRange;
import com.capstone.pick.dto.CommentDto;
import com.capstone.pick.dto.CommentLikeDto;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.exeption.BookmarkNotFoundException;
import com.capstone.pick.exeption.UserMismatchException;
import com.capstone.pick.repository.*;
import com.capstone.pick.repository.cache.CommentLikeCacheRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.LocalDateTime;
import java.util.Optional;

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
    @Mock private CommentLikeCacheRepository commentLikeRedisRepository;

    @DisplayName("댓글 상세 보기 페이지를 조회하면, 해당 투표 게시글에 대한 투표 댓글을 반환한다.")
    @Test
    void readComment() {
        // given
        User user1 = createUser("user1", "nick1");
        Vote vote = createVote(1L, user1);

        Pageable pageable = mock(Pageable.class);
        Page<VoteComment> comments = mock(Page.class);

        given(voteRepository.findById(any())).willReturn(Optional.of(vote));
        given(voteCommentRepository.findAllByVote(vote, pageable)).willReturn(comments);

        // when
        voteCommentService.commentsByVote(1L, pageable);

        // then
        then(voteCommentRepository).should().findAllByVote(any(), any());
    }

    @DisplayName("투표 댓글을 입력하면, 투표 댓글을 저장한다.")
    @Test
    void saveComment() {
        // given
        User user1 = createUser("user1", "nick1");
        Vote vote = createVote(1L, user1);
        CommentDto commentDto = createCommentDto(vote.getId(), UserDto.from(user1), "content1");

        given(userRepository.getReferenceById(anyString())).willReturn(user1);
        given(voteRepository.getReferenceById(anyLong())).willReturn(vote);

        // when
        voteCommentService.saveComment(commentDto);

        // then
        then(userRepository).should().getReferenceById(anyString());
        then(voteRepository).should().getReferenceById(anyLong());
        then(voteCommentRepository).should().save(any(VoteComment.class));
    }

    @DisplayName("댓글 수정 정보를 입력하면, 투표 댓글을 수정한다.")
    @Test
    void updateComment() throws UserMismatchException {
        // given
        User user1 = createUser("user1", "nick1");
        Vote vote = createVote(1L, user1);
        VoteComment voteComment = createVoteComment(1L, user1, vote, "content1", LocalDateTime.now());

        CommentDto commentDto = createCommentDto(vote.getId(), UserDto.from(user1), "content2");

        given(voteCommentRepository.getReferenceById(anyLong())).willReturn(voteComment);
        given(userRepository.getReferenceById(anyString())).willReturn(user1);

        // when
        voteCommentService.updateComment(voteComment.getId(), commentDto);

        // then
        assertThat(voteComment).hasFieldOrPropertyWithValue("content", commentDto.getContent());
        then(voteCommentRepository).should().getReferenceById(anyLong());
        then(userRepository).should().getReferenceById(anyString());
    }

    @DisplayName("댓글 id를 입력하면, 투표 댓글을 삭제한다.")
    @Test
    void deleteComment() throws UserMismatchException {
        // given
        User user1 = createUser("user1", "nick1");
        Vote vote = createVote(1L, user1);
        VoteComment voteComment = createVoteComment(1L, user1, vote, "content1", LocalDateTime.now());

        given(userRepository.getReferenceById(anyString())).willReturn(user1);
        given(voteCommentRepository.getReferenceById(anyLong())).willReturn(voteComment);

        // when
        voteCommentService.deleteComment(voteComment.getId(), user1.getUserId());

        // then
        then(voteCommentRepository).should().delete(any(VoteComment.class));
    }

    @DisplayName("댓글 좋아요를 저장한다.")
    @Test
    void saveCommentLike() {
        // given
        User user1 = createUser("user1", "nick1");
        Vote vote = createVote(1L, user1);
        VoteComment voteComment = createVoteComment(1L, user1, vote, "content", LocalDateTime.now());
        CommentLike commentLike = createCommentLike(1L, voteComment, user1);

        given(userRepository.getReferenceById(anyString())).willReturn(user1);
        given(voteCommentRepository.getReferenceById(anyLong())).willReturn(voteComment);
        given(commentLikeRepository.save(any(CommentLike.class))).willReturn(commentLike);

        // when
        voteCommentService.saveCommentLike(CommentLikeDto.from(commentLike));

        // then
        then(userRepository).should().getReferenceById(anyString());
        then(voteCommentRepository).should().getReferenceById(anyLong());
        then(commentLikeRepository).should().save(any(CommentLike.class));
        then(commentLikeRedisRepository).should().save(any(CommentLikeDto.class));
    }

    @DisplayName("댓글 좋아요를 삭제한다.")
    @Test
    void deleteCommentLike() throws UserMismatchException {
        // given
        User user1 = createUser("user1", "nick1");
        Vote vote = createVote(1L, user1);
        VoteComment voteComment = createVoteComment(1L, user1, vote, "content", LocalDateTime.now());
        CommentLike like = createCommentLike(1L, voteComment, user1);

        given(userRepository.getReferenceById(anyString())).willReturn(user1);
        given(voteCommentRepository.getReferenceById(anyLong())).willReturn(voteComment);
        given(commentLikeRepository.findByUserAndVoteComment(user1, voteComment)).willReturn(Optional.of(like));

        // when
        voteCommentService.deleteCommentLike(voteComment.getId(), user1.getUserId());

        // then
        then(userRepository).should().getReferenceById(anyString());
        then(voteCommentRepository).should().getReferenceById(anyLong());
        then(commentLikeRepository).should().findByUserAndVoteComment(any(User.class), any(VoteComment.class));
        then(commentLikeRepository).should().delete(any(CommentLike.class));
    }


    @DisplayName("댓글에 눌린 좋아요 개수를 반환한다.")
    @Test
    void getLikeCount() {
        // given
        Long commentId = 1L;
        given(commentLikeRepository.countByVoteCommentId(anyLong())).willReturn(1L);

        // when
        voteCommentService.getLikeCount(commentId);

        // then
        then(commentLikeRepository).should().countByVoteCommentId(anyLong());
    }

    @DisplayName("댓글 id와 유저 id를 받으면, 존재하는 좋아요 id를 반환한다.")
    @Test
    void findLikeId() {
        // given
        Long commentId = 1L;
        String userId = "user";

        // when
        voteCommentService.findLikeId(commentId, userId);

        // then
        then(commentLikeRepository).should().findByVoteCommentIdAndUserUserId(anyLong(), anyString());
    }

    private static User createUser(String userId, String nickname) {
        return User.builder()
                .userId(userId)
                .userPassword("password")
                .email("email@email.com")
                .nickname(nickname)
                .memo("memo")
                .birthday("0101")
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

    private static VoteComment createVoteComment(Long id, User user, Vote vote, String content, LocalDateTime createTime) {
        VoteComment voteComment = VoteComment.builder()
                .user(user)
                .vote(vote)
                .createAt(createTime)
                .content(content)
                .build();
        ReflectionTestUtils.setField(voteComment, "id", id);
        return voteComment;
    }

    private static CommentDto createCommentDto(Long voteId, UserDto userDto, String content) {
        return CommentDto.builder()
                .voteId(voteId)
                .userDto(userDto)
                .content(content)
                .createAt(LocalDateTime.now())
                .build();
    }

    private static CommentLike createCommentLike(Long id, VoteComment voteComment, User user) {
        CommentLike like = CommentLike.builder()
                .voteComment(voteComment)
                .user(user)
                .build();
        ReflectionTestUtils.setField(like, "id", id);
        return like;
    }

    private static CommentLikeDto createLikeDto(Long commentId, UserDto userDto) {
        return CommentLikeDto.builder()
                .userDto(userDto)
                .voteCommentId(commentId)
                .build();
    }
}
