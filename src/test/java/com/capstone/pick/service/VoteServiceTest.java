package com.capstone.pick.service;

import com.capstone.pick.domain.*;
import com.capstone.pick.domain.constant.Category;
import com.capstone.pick.domain.constant.DisplayRange;
import com.capstone.pick.domain.constant.OrderCriteria;
import com.capstone.pick.dto.*;
import com.capstone.pick.repository.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Sort;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 서비스 로직 - 투표 게시글")
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = "com.capstone.pick.config")
@MockBean(JpaMetamodelMappingContext.class)
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
        List<Vote> votes = new ArrayList<>();

        User user = createUser();

        Vote vote1 = createVote(1L, user, "title1", "content1", Category.FREE, LocalDateTime.now());
        Vote vote2 = createVote(2L, user, "title2", "content2", Category.FREE, LocalDateTime.now());

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

    @Disabled("[타임라인 조회] - 더이상 사용하지 않는 메소드에 대한 테스트케이스")
    @DisplayName("타임라인을 조회하면, 카테고리와 정렬기준에 따른 투표 게시글을 타임라인에 반환한다.")
    @Test
    void findSortedVotesByCategory() {
        // given
        User user = createUser();
        Vote vote1 = createVote(1L, user, "title1", "content1", Category.FREE, LocalDateTime.now());
        Vote vote2 = createVote(2L, user, "title2", "content2", Category.FREE, LocalDateTime.now().minusHours(1));
        Vote vote3 = createVote(3L, user, "title3", "content3", Category.WORRY, LocalDateTime.now().minusHours(2));
        VoteOption option1 = createVoteOption(vote1, "option1", "/image/link1");
        VoteOption option2 = createVoteOption(vote1, "option2", "/image/link2");
        VoteOption option3 = createVoteOption(vote2, "option1", "/image/link3");
        VoteOption option4 = createVoteOption(vote3, "option4", "/image/link4");
        ReflectionTestUtils.setField(option1, "id", 1L);
        ReflectionTestUtils.setField(option2, "id", 2L);
        ReflectionTestUtils.setField(option3, "id", 3L);
        ReflectionTestUtils.setField(option4, "id", 4L);
        Pick pick1 = createPick(user, option1);
        Pick pick2 = createPick(user, option2);
        Pick pick3 = createPick(user, option3);
        ReflectionTestUtils.setField(pick1, "id", 1L);
        ReflectionTestUtils.setField(pick2, "id", 2L);
        ReflectionTestUtils.setField(pick3, "id", 3L);

        given(voteRepository.findAll(Sort.by(Sort.Direction.DESC, "modifiedAt"))).willReturn(List.of(vote1, vote2, vote3));
        given(voteRepository.findByCategory(Category.FREE, Sort.by(Sort.Direction.DESC, "modifiedAt"))).willReturn(List.of(vote1, vote2));
        given(voteRepository.findAllOrderByPopular()).willReturn(List.of(vote2, vote1, vote3));
        given(voteRepository.findByCategoryOrderByPopular(Category.FREE)).willReturn(List.of(vote2, vote1));

        // when
        List<VoteOptionCommentDto> All_LATEST = voteService.findSortedVotesByCategory(Category.ALL, OrderCriteria.LATEST);
        List<VoteOptionCommentDto> All_POPULAR = voteService.findSortedVotesByCategory(Category.ALL, OrderCriteria.POPULAR);
        List<VoteOptionCommentDto> FREE_LATEST = voteService.findSortedVotesByCategory(Category.FREE, OrderCriteria.LATEST);
        List<VoteOptionCommentDto> FREE_POPULAR = voteService.findSortedVotesByCategory(Category.FREE, OrderCriteria.POPULAR);

        // then
        assertThat(All_LATEST.get(0))
                .hasFieldOrPropertyWithValue("title", vote1.getTitle())
                .hasFieldOrPropertyWithValue("category", vote1.getCategory())
                .hasFieldOrPropertyWithValue("modifiedAt", vote1.getModifiedAt());
        assertThat(All_POPULAR.get(0))
                .hasFieldOrPropertyWithValue("title", vote2.getTitle())
                .hasFieldOrPropertyWithValue("category", vote2.getCategory())
                .hasFieldOrPropertyWithValue("modifiedAt", vote2.getModifiedAt());
        assertThat(FREE_LATEST.get(0))
                .hasFieldOrPropertyWithValue("title", vote1.getTitle())
                .hasFieldOrPropertyWithValue("category", vote1.getCategory())
                .hasFieldOrPropertyWithValue("modifiedAt", vote1.getModifiedAt());
        assertThat(FREE_POPULAR.get(0))
                .hasFieldOrPropertyWithValue("title", vote2.getTitle())
                .hasFieldOrPropertyWithValue("category", vote2.getCategory())
                .hasFieldOrPropertyWithValue("modifiedAt", vote2.getModifiedAt());

        assertThat(All_LATEST.size()).isEqualTo(3);
        assertThat(All_POPULAR.size()).isEqualTo(3);
        assertThat(FREE_LATEST.size()).isEqualTo(2);
        assertThat(FREE_POPULAR.size()).isEqualTo(2);
    }

    @DisplayName("투표 게시글 정보를 입력하면, 투표 게시글을 저장한다.")
    @Test
    void saveVote() {
        // given
        User user = createUser();
        Vote vote = createVote(1L, user, "new title", "new content #hi", Category.FREE, LocalDateTime.now());
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
        Vote vote = createVote(1L, user, "title", "content", Category.FREE, LocalDateTime.now());
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

    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다.")
    @Test
    void deleteVote() {
        // given
        Long voteId = 1L;
        String userId = "user";

        willDoNothing().given(voteRepository).deleteByIdAndUser_UserId(voteId, userId);

        // when
        voteService.deleteVote(voteId, userId);

        // then
        then(voteRepository).should().deleteByIdAndUser_UserId(voteId, userId);
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

    private static Pick createPick(User user, VoteOption voteOption) {
        return Pick.builder()
                .user(user)
                .voteOption(voteOption)
                .build();
    }

    private static Hashtag createHashtag(String content) {
        return Hashtag.builder().content(content).build();
    }

    private static Vote createVote(Long id, User user, String title, String content, Category category, LocalDateTime createAt) {
        Vote vote = Vote.builder()
                .user(user)
                .title(title)
                .content(content)
                .category(category)
                .createAt(createAt)
                .expiredAt(LocalDateTime.now().plusDays(3))
                .isMultiPick(true)
                .displayRange(DisplayRange.PUBLIC)
                .voteOptions(new ArrayList<>())
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
                .birthday("0101")
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
                .birthday("0101")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
