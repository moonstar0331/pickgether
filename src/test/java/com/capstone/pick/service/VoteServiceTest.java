package com.capstone.pick.service;

import com.capstone.pick.domain.*;
import com.capstone.pick.domain.constant.Category;
import com.capstone.pick.domain.constant.DisplayRange;
import com.capstone.pick.dto.HashtagDto;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.dto.VoteDto;
import com.capstone.pick.dto.VoteOptionDto;
import com.capstone.pick.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@DisplayName("비즈니스 서비스 로직 - 투표 게시글")
@ExtendWith(MockitoExtension.class)
public class VoteServiceTest {

    @InjectMocks
    private VoteService voteService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private VoteRepository voteRepository;
    @Mock
    private VoteOptionRepository voteOptionRepository;
    @Mock
    private HashtagRepository hashtagRepository;
    @Mock
    private VoteHashtagRepository voteHashtagRepository;

    @DisplayName("타임라인을 조회하면, 모든 투표 게시글을 타임라인에 반환한다.")
    @Test
    void findAllVotes() {
        // given
        Long voteId = 1L;
        List<Vote> votes = new ArrayList<>();

        User user = createUser();

        Vote vote1 = createVote(1L, user, "title1", "content1");
        Vote vote2 = createVote(2L, user, "title2", "content2");

        votes.add(vote1);
        votes.add(vote2);

        given(voteRepository.findAll()).willReturn(votes);

        // when
        List<VoteDto> voteDtos = voteService.findAllVotes();

        // then
        assertThat(votes.get(0))
                .hasFieldOrPropertyWithValue("title", vote1.getTitle())
                .hasFieldOrPropertyWithValue("content", vote1.getContent())
                .hasFieldOrPropertyWithValue("category", vote1.getCategory())
                .hasFieldOrPropertyWithValue("createAt", vote1.getCreateAt())
                .hasFieldOrPropertyWithValue("modifiedAt", vote1.getModifiedAt())
                .hasFieldOrPropertyWithValue("isMultiPick", vote1.isMultiPick())
                .hasFieldOrPropertyWithValue("displayRange", vote1.getDisplayRange());

        assertThat(voteDtos.size()).isEqualTo(2);
    }

    @DisplayName("투표 게시글 정보를 입력하면, 투표 게시글을 저장한다.")
    @Test
    void saveVote() {
        // given
        User user = createUser();
        Vote vote = createVote(1L, user, "new title", "new content #hi");
        VoteOption option1 = createVoteOption(vote, "option1", "/image/link1");
        VoteOption option2 = createVoteOption(vote, "option2", "/image/link2");
        List<VoteOptionDto> options = List.of(VoteOptionDto.from(option1), VoteOptionDto.from(option2));
        ReflectionTestUtils.setField(option1, "id", 1L);
        ReflectionTestUtils.setField(option2, "id", 2L);
        Hashtag hashtag = createHashtag("hi");
        List<Hashtag> hashtags = List.of(hashtag);
        List<HashtagDto> hashtagDtos = List.of(HashtagDto.from(hashtag));
        ReflectionTestUtils.setField(hashtag, "id", 1L);
        VoteHashtag voteHashtag = createVoteHashtag(vote, hashtag);
        ReflectionTestUtils.setField(voteHashtag, "id", 1L);

        given(userRepository.getReferenceById(anyString())).willReturn(user);
        given(voteRepository.save(any(Vote.class))).willReturn(vote);
        options.forEach(o -> voteOptionRepository.save(o.toEntity(vote)));
        hashtagDtos.stream().map(h -> given(hashtagRepository.save(h.toEntity())).willReturn(h.toEntity()));
        hashtags.forEach(h -> voteHashtagRepository.save(createVoteHashtag(vote, hashtag)));

        // when
        voteService.saveVote(VoteDto.from(vote), List.of(VoteOptionDto.from(option1), VoteOptionDto.from(option2)), List.of(HashtagDto.from(hashtag)));

        // then
        then(userRepository).should().getReferenceById(anyString());
        then(voteRepository).should().save(any(Vote.class));
        verify(voteOptionRepository, atLeastOnce()).save(any(VoteOption.class));
        then(hashtagRepository).should().save(any(Hashtag.class));
        verify(voteHashtagRepository, atLeastOnce()).save(any(VoteHashtag.class));
    }

    @DisplayName("투표 게시글의 수정 정보를 입력하면, 투표 게시글을 수정한다.")
    @Test
    void updateVote() {
        // given
        User user = createUser();
        Vote vote = createVote(1L, user, "title", "content");
        VoteDto voteDto = createVoteDto("new title", "new content");

        VoteOption option1 = createVoteOption(vote, "option1", "/image/link1");
        VoteOption option2 = createVoteOption(vote, "option2", "/image/link2");
        ReflectionTestUtils.setField(option1, "id", 1L);
        ReflectionTestUtils.setField(option2, "id", 2L);
        List<VoteOptionDto> options = List.of(VoteOptionDto.from(option1), VoteOptionDto.from(option2));

        Hashtag hashtag = createHashtag("hi");
        List<HashtagDto> hashtagDtos = List.of(HashtagDto.from(hashtag));
        ReflectionTestUtils.setField(hashtag, "id", 1L);

        given(voteRepository.getReferenceById(voteDto.getId())).willReturn(vote);
        given(userRepository.getReferenceById(voteDto.getUserDto().getUserId())).willReturn(user);

        // when
        voteService.updateVote(voteDto.getId(), voteDto, options, hashtagDtos);

        // then
        assertThat(vote)
                .hasFieldOrPropertyWithValue("title", voteDto.getTitle())
                .hasFieldOrPropertyWithValue("content", voteDto.getContent());
        then(voteRepository).should().getReferenceById(voteDto.getId());
        then(userRepository).should().getReferenceById(voteDto.getUserDto().getUserId());
    }

    @DisplayName("존재하지 않는 투표 게시글에 수정 정보를 입력하면, 경고 로그를 출력하고 아무 것도 하지 않는다.")
    @Test
    void updateVote_Exception() {
        // given
        VoteDto voteDto = createVoteDto("new title", "new content");

        User user = createUser();
        VoteOption option1 = createVoteOption(voteDto.toEntity(user), "option1", "/image/link1");
        VoteOption option2 = createVoteOption(voteDto.toEntity(user), "option2", "/image/link2");
        ReflectionTestUtils.setField(option1, "id", 1L);
        ReflectionTestUtils.setField(option2, "id", 2L);
        List<VoteOptionDto> options = List.of(VoteOptionDto.from(option1), VoteOptionDto.from(option2));

        Hashtag hashtag = createHashtag("hi");
        List<HashtagDto> hashtagDtos = List.of(HashtagDto.from(hashtag));
        ReflectionTestUtils.setField(hashtag, "id", 1L);

        given(voteRepository.getReferenceById(voteDto.getId())).willThrow(EntityNotFoundException.class);

        // when
        voteService.updateVote(voteDto.getId(), voteDto, options, hashtagDtos);

        // then
        then(voteRepository).should().getReferenceById(voteDto.getId());
    }

    private static VoteDto createVoteDto(String title, String content) {
        return VoteDto.builder()
                .id(1L)
                .userDto(createUserDto())
                .title(title)
                .content(content)
                .category(Category.FREE)
                .expiredAt(LocalDateTime.now().plusDays(5))
                .createAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .isMultiPick(true)
                .displayRange(DisplayRange.PUBLIC)
                .build();
    }

    private static VoteHashtag createVoteHashtag(Vote vote, Hashtag hashtag) {
        return VoteHashtag.builder()
                .vote(vote)
                .hashtag(hashtag)
                .build();
    }

    private static VoteOption createVoteOption(Vote vote, String content, String imageLink) {
        return VoteOption.builder()
                .vote(vote)
                .content(content)
                .imageLink(imageLink)
                .build();
    }

    private static Hashtag createHashtag(String content) {
        return Hashtag.builder().content(content).build();
    }

    private static Vote createVote(Long id, User user, String title, String content) {
        Vote vote = Vote.builder()
                .user(user)
                .title(title)
                .content(content)
                .category(Category.FREE)
                .createAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusDays(3))
                .isMultiPick(true)
                .displayRange(DisplayRange.PUBLIC)
                .build();
        ReflectionTestUtils.setField(vote, "id", id);
        return vote;
    }

    private static User createUser() {
        return User.builder()
                .userId("user")
                .userPassword("password")
                .email("email@email.com")
                .nickname("nick")
                .memo("memo")
                .birthday(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private static UserDto createUserDto() {
        return UserDto.builder()
                .userId("user")
                .userPassword("password")
                .email("email@email.com")
                .nickname("nick")
                .memo("memo")
                .birthday(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
