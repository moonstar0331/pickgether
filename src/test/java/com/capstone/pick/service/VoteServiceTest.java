package com.capstone.pick.service;

import com.capstone.pick.domain.*;
import com.capstone.pick.domain.constant.Category;
import com.capstone.pick.domain.constant.DisplayRange;
import com.capstone.pick.domain.constant.SearchType;
import com.capstone.pick.dto.*;
import com.capstone.pick.exeption.UserMismatchException;
import com.capstone.pick.repository.*;
import com.capstone.pick.repository.cache.BookmarkCacheRepository;
import com.capstone.pick.repository.cache.VoteRedisRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @Mock
    private VoteRedisRepository voteRedisRepository;
    @Mock
    private BookmarkRepository bookmarkRepository;
    @Mock
    private BookmarkCacheRepository bookmarkCacheRepository;

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

    @DisplayName("타임라인을 조회하면, 카테고리와 정렬기준에 따른 투표 게시글을 타임라인에 반환한다.")
    @Test
    void viewTimeline() {
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
        Pageable pageable = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "modifiedAt"));

        given(voteRepository.findAll(pageable)).willReturn(Page.empty());
        given(voteRepository.findAllByCategory(Category.FREE, pageable)).willReturn(Page.empty());

        // when
        Page<VoteOptionCommentDto> All_LATEST = voteService.viewTimeLine(Category.ALL, pageable);
        Page<VoteOptionCommentDto> FREE_LATEST = voteService.viewTimeLine(Category.FREE, pageable);

        // then
        then(voteRepository).should().findAll(any(Pageable.class));
        then(voteRepository).should().findAllByCategory(any(Category.class), any(Pageable.class));
    }

    @DisplayName("제목을 검색하면, 해당하는 투표 게시글을 반환한다.")
    @Test
    void searchVotes_TITLE() {
        // given
        User user = createUser();
        Vote vote1 = createVote(1L, user, "title", "content1 #hashtag1", Category.FREE, LocalDateTime.now());
        Vote vote2 = createVote(2L, user, "title1", "content #hashtag1", Category.FREE, LocalDateTime.now().minusHours(1));
        Vote vote3 = createVote(3L, user, "title1", "content1 #hashtag", Category.WORRY, LocalDateTime.now().minusHours(2));
        List<Vote> list = List.of(vote2, vote3);
        given(voteRepository.findByTitleContaining("title1")).willReturn(list);

        // when
        List<VoteWithOptionDto> votes = voteService.searchVotes(SearchType.TITLE, "title1");

        // then
        then(voteRepository).should().findByTitleContaining(anyString());
        assertThat(votes).hasSize(list.size());
    }

    @DisplayName("내용을 검색하면, 해당하는 투표 게시글을 반환한다.")
    @Test
    void searchVotes_CONTENT() {
        // given
        User user = createUser();
        Vote vote1 = createVote(1L, user, "title", "content1 #hashtag1", Category.FREE, LocalDateTime.now());
        Vote vote2 = createVote(2L, user, "title1", "content #hashtag1", Category.FREE, LocalDateTime.now().minusHours(1));
        Vote vote3 = createVote(3L, user, "title1", "content1 #hashtag", Category.WORRY, LocalDateTime.now().minusHours(2));
        List<Vote> list = List.of(vote1, vote3);
        given(voteRepository.findByContentContaining("content1")).willReturn(list);

        // when
        List<VoteWithOptionDto> votes = voteService.searchVotes(SearchType.CONTENT, "content1");

        // then
        then(voteRepository).should().findByContentContaining(anyString());
        assertThat(votes).hasSize(list.size());
    }

    @DisplayName("해시태그를 검색하면, 해당하는 투표 게시글을 반환한다.")
    @Test
    void searchVotes_HASHTAG() {
        // given
        User user = createUser();
        Vote vote1 = createVote(1L, user, "title", "content1 #hashtag1", Category.FREE, LocalDateTime.now());
        Vote vote2 = createVote(2L, user, "title1", "content #hashtag1", Category.FREE, LocalDateTime.now().minusHours(1));
        Vote vote3 = createVote(3L, user, "title1", "content1 #hashtag", Category.WORRY, LocalDateTime.now().minusHours(2));
        Hashtag hashtag1 = Hashtag.builder().id(1L).content("hashtag1").build();
        Hashtag hashtag2 = Hashtag.builder().id(2L).content("hashtag1").build();
        Hashtag hashtag3 = Hashtag.builder().id(3L).content("hashtag").build();
        VoteHashtag voteHashtag1 = VoteHashtag.builder().vote(vote1).hashtag(hashtag1).build();
        VoteHashtag voteHashtag2 = VoteHashtag.builder().vote(vote2).hashtag(hashtag2).build();
        VoteHashtag voteHashtag3 = VoteHashtag.builder().vote(vote3).hashtag(hashtag3).build();
        List<VoteHashtag> hashtagList = List.of(voteHashtag2, voteHashtag3);
        List<Vote> list = List.of(vote1, vote2);
        given(voteHashtagRepository.findByHashtag_ContentContaining("hashtag1")).willReturn(hashtagList);

        // when
        List<VoteWithOptionDto> votes = voteService.searchVotes(SearchType.HASHTAG, "hashtag1");

        // then
        then(voteHashtagRepository).should().findByHashtag_ContentContaining(anyString());
        assertThat(votes).hasSize(list.size());
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

    @DisplayName("게시글 ID에 해당하는 게시글(VoteDto)을 반환한다.")
    @Test
    void getVote() {
        // given
        User user = createUser();
        Long voteId = 1L;
        Vote vote = createVote(voteId, user, "title1", "content1", Category.FREE, LocalDateTime.now());
        given(voteRepository.getReferenceById(voteId)).willReturn(vote);

        // when
        voteService.getVote(voteId);

        // then
        then(voteRepository).should().getReferenceById(anyLong());
    }

    @DisplayName("게시글 ID에 해당하는 게시글(VoteWithOptionDto)을 반환한다.")
    @Test
    void getVoteWithOption() {
        // given
        User user = createUser();
        Long voteId = 1L;
        Vote vote = createVote(voteId, user, "title1", "content1", Category.FREE, LocalDateTime.now());
        given(voteRepository.getReferenceById(voteId)).willReturn(vote);

        // when
        voteService.getVoteWithOption(voteId);

        // then
        then(voteRepository).should().getReferenceById(anyLong());
    }

    @DisplayName("게시글 ID에 해당하는 게시글 선택지을 반환한다.")
    @Test
    void getOptions() {
        // given
        User user = createUser();
        Long voteId = 1L;
        Vote vote = createVote(voteId, user, "title1", "content1", Category.FREE, LocalDateTime.now());
        VoteOption option1 = createVoteOption(vote, "option1", "/image/link1");
        VoteOption option2 = createVoteOption(vote, "option2", "/image/link2");
        given(voteOptionRepository.findAllByVoteId(voteId)).willReturn(List.of(option1, option2));

        // when
        voteService.getOptions(voteId);

        // then
        then(voteOptionRepository).should().findAllByVoteId(anyLong());
    }

    @DisplayName("userId와 voteId를 입력받았을 때, 저장된 북마크가 존재한다면 저장한다.")
    @Test
    void saveBookmark() {
        // given
        User user = createUser();
        Long voteId = 1L;
        Vote vote = createVote(voteId, user, "title1", "content1", Category.FREE, LocalDateTime.now());
        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .vote(vote)
                .build();

        given(userRepository.getReferenceById(user.getUserId())).willReturn(user);
        given(voteRepository.getReferenceById(voteId)).willReturn(vote);
        given(bookmarkRepository.findByUserAndVote(user, vote)).willReturn(Optional.empty());
        given(bookmarkRepository.save(any(Bookmark.class))).willReturn(bookmark);

        // when
        voteService.saveBookmark(user.getUserId(), voteId);

        // then
        then(userRepository).should().getReferenceById(anyString());
        then(voteRepository).should().getReferenceById(anyLong());
        then(bookmarkRepository).should().findByUserAndVote(any(User.class), any(Vote.class));
        then(bookmarkRepository).should().save(any(Bookmark.class));
        then(bookmarkCacheRepository).should().setBookmark(any(BookmarkDto.class));
        assertEquals(user, bookmark.getUser());
        assertEquals(vote, bookmark.getVote());
    }

    @DisplayName("userId와 voteId를 입력받았을 때, 저장된 북마크가 존재한다면 예외를 발생한다.")
    @Test
    void saveBookmark_whenBookmarkExists() {
        // given
        User user = createUser();
        Long voteId = 1L;
        Vote vote = createVote(voteId, user, "title1", "content1", Category.FREE, LocalDateTime.now());
        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .vote(vote)
                .build();

        given(userRepository.getReferenceById(user.getUserId())).willReturn(user);
        given(voteRepository.getReferenceById(voteId)).willReturn(vote);
        given(bookmarkRepository.findByUserAndVote(user, vote)).willReturn(Optional.of(bookmark));

        // when & then
        Throwable exception = assertThrows(RuntimeException.class, () -> {
            voteService.saveBookmark(user.getUserId(), voteId);
        });
        assertEquals("이미 저장된 게시글입니다.", exception.getMessage());
    }

    @DisplayName("userId와 voteId를 입력받았을 때, 북마크의 유저정보와 userId가 일치한다면 삭제한다.")
    @Test
    void deleteBookmark() throws UserMismatchException {
        // given
        User user = createUser();
        Long voteId = 1L;
        Vote vote = createVote(voteId, user, "title1", "content1", Category.FREE, LocalDateTime.now());
        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .vote(vote)
                .build();

        given(userRepository.getReferenceById(user.getUserId())).willReturn(user);
        given(bookmarkRepository.findByUserAndVoteId(user, voteId)).willReturn(bookmark);

        // when
        voteService.deleteBookmark(user.getUserId(), voteId);

        // then
        then(userRepository).should().getReferenceById(anyString());
        then(bookmarkRepository).should().findByUserAndVoteId(any(User.class), anyLong());
        then(bookmarkRepository).should().delete(any(Bookmark.class));
    }

    @DisplayName("userId를 입력받았을 때, 해당 유저가 저장한 북마크를 모두 삭제한다.")
    @Test
    void deleteAllBookmark(){
        // given
        User user = createUser();

        given(userRepository.getReferenceById(user.getUserId())).willReturn(user);
        willDoNothing().given(bookmarkRepository).deleteByUser(user);

        // when
        voteService.deleteAllBookmark(user.getUserId());

        // then
        then(userRepository).should().getReferenceById(anyString());
        then(bookmarkRepository).should().deleteByUser(any(User.class));
    }

    @DisplayName("북마크 페이지를 조회하면, 해당 유저에 대한 북마크를 반환한다.")
    @Test
    void viewBookmarks() {
        // given
        User user = createUser();
        Vote vote = createVote(1L, user, "title1", "content1", Category.FREE, LocalDateTime.now());

        Pageable pageable = mock(Pageable.class);
        Page<Bookmark> bookmarks = mock(Page.class);

        given(userRepository.getReferenceById(user.getUserId())).willReturn(user);
        given(bookmarkRepository.findAllByUser(user, pageable)).willReturn(bookmarks);

        // when
        voteService.viewBookmarks(user.getUserId(), pageable);

        // then
        then(bookmarkRepository).should().findAllByUser(any(User.class), any());
    }

    @DisplayName("userId를 입력받으면, 해당 유저가 저장한 북마크의 투표 게시글 ID(voteId) 리스트를 반환한다.")
    @Test
    void findBookmarkVoteId() {
        // given
        User user = createUser();

        given(userRepository.getReferenceById(user.getUserId())).willReturn(user);
        given(bookmarkRepository.findAllByUser(user)).willReturn(anyList());

        // when
        voteService.findBookmarkVoteId(user.getUserId());

        // then
        then(bookmarkRepository).should().findAllByUser(any(User.class));
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
