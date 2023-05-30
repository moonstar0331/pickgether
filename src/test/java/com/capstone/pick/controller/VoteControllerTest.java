package com.capstone.pick.controller;

import com.capstone.pick.config.TestSecurityConfig;
import com.capstone.pick.controller.form.SearchForm;
import com.capstone.pick.controller.form.VoteForm;
import com.capstone.pick.controller.form.VoteOptionFormDto;
import com.capstone.pick.domain.User;
import com.capstone.pick.domain.constant.Category;
import com.capstone.pick.domain.constant.DisplayRange;
import com.capstone.pick.domain.constant.SearchType;
import com.capstone.pick.dto.*;
import com.capstone.pick.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("투표 컨트롤러")
@Import(TestSecurityConfig.class)
@WebMvcTest(VoteController.class)
@ComponentScan(basePackages = "com.capstone.pick.config")
@MockBean(JpaMetamodelMappingContext.class)
class VoteControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private VoteService voteService;

    @MockBean
    private VoteCommentService voteCommentService;

    @MockBean
    private UserService userService;

    @MockBean private FollowService followService;

    @MockBean
    private VoteResultService voteResultService;

    @MockBean
    private PickService pickService;

    @MockBean
    private FileUploadService fileUploadService;

    public VoteControllerTest(@Autowired MockMvc mvc, @Autowired ObjectMapper objectMapper) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[GET][/] 홈페이지 - 정상 호출, 인증된 사용자")
    @Test
    void home() throws Exception {
        // when & then
        mvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/timeline"))
                .andExpect(redirectedUrl("/timeline"));
    }

    @DisplayName("[GET][/] 홈페이지 - 인증이 없을 시에는 로그인 페이지로 이동")
    @Test
    void noLoginUser_home() throws Exception {
        // when & then
        mvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[GET][/search] 검색 페이지 - 정상 호출, 인증된 사용자")
    @Test
    void search() throws Exception {
        // when & then
        mvc.perform(get("/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("page/search"));
    }

    @DisplayName("[GET][/search] 검색 페이지 - 인증이 없을 시에는 로그인 페이지로 이동")
    @Test
    void search_NoLoginUser() throws Exception {
        // when & then
        mvc.perform(get("/search"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[POST][/search] 유저 검색 - 정상 호출, 인증된 사용자")
    @Test
    void search_user() throws Exception {
        // given
        SearchForm searchForm = SearchForm.builder()
                .searchType(SearchType.USER)
                .searchValue("u")
                .build();

        // when
        mvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("searchForm", searchForm)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("page/search :: #searchResult"))
                .andExpect(model().attributeExists("users"));

        // then
        then(userService).should().searchUsers(any(SearchType.class), anyString());
    }

    @DisplayName("[POST][/search] 유저 검색 - 인증이 없을 시에는 에러 발생")
    @Test
    void search_user_NoLoginUser() throws Exception {
        // given
        SearchForm searchForm = SearchForm.builder()
                .searchType(SearchType.USER)
                .searchValue("u")
                .build();

        // when
        mvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("searchForm", searchForm))
                .andDo(print())
                .andExpect(status().isForbidden());

        // then
        then(userService).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[POST][/search] 닉네임 검색 - 정상 호출, 인증된 사용자")
    @Test
    void search_nickname() throws Exception {
        // given
        SearchForm searchForm = SearchForm.builder()
                .searchType(SearchType.NICKNAME)
                .searchValue("n")
                .build();

        // when
        mvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("searchForm", searchForm)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("page/search :: #searchResult"))
                .andExpect(model().attributeExists("users"));

        // then
        then(userService).should().searchUsers(any(SearchType.class), anyString());
    }

    @DisplayName("[POST][/search] 닉네임 검색 - 인증이 없을 시에는 에러 발생")
    @Test
    void search_nickname_NoLoginUser() throws Exception {
        // given
        SearchForm searchForm = SearchForm.builder()
                .searchType(SearchType.NICKNAME)
                .searchValue("n")
                .build();

        // when
        mvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("searchForm", searchForm))
                .andDo(print())
                .andExpect(status().isForbidden());

        // then
        then(userService).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[POST][/search] 제목 검색 - 정상 호출, 인증된 사용자")
    @Test
    void search_title() throws Exception {
        // given
        SearchForm searchForm = SearchForm.builder()
                .searchType(SearchType.TITLE)
                .searchValue("t")
                .build();

        // when
        mvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("searchForm", searchForm)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("page/search :: #searchResult"))
                .andExpect(model().attributeExists("votes"));

        // then
        then(voteService).should().searchVotes(any(SearchType.class), anyString());
    }

    @DisplayName("[POST][/search] 제목 검색 - 인증이 없을 시에는 에러 발생")
    @Test
    void search_title_NoLoginUser() throws Exception {
        // given
        SearchForm searchForm = SearchForm.builder()
                .searchType(SearchType.TITLE)
                .searchValue("t")
                .build();

        // when
        mvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("searchForm", searchForm))
                .andDo(print())
                .andExpect(status().isForbidden());

        // then
        then(voteService).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[POST][/search] 내용 검색 - 정상 호출, 인증된 사용자")
    @Test
    void search_content() throws Exception {
        // given
        SearchForm searchForm = SearchForm.builder()
                .searchType(SearchType.CONTENT)
                .searchValue("c")
                .build();

        // when
        mvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("searchForm", searchForm)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("page/search :: #searchResult"))
                .andExpect(model().attributeExists("votes"));

        // then
        then(voteService).should().searchVotes(any(SearchType.class), anyString());
    }

    @DisplayName("[POST][/search] 내용 검색 - 인증이 없을 시에는 에러 발생")
    @Test
    void search_content_NoLoginUser() throws Exception {
        // given
        SearchForm searchForm = SearchForm.builder()
                .searchType(SearchType.CONTENT)
                .searchValue("c")
                .build();

        // when
        mvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("searchForm", searchForm))
                .andDo(print())
                .andExpect(status().isForbidden());

        // then
        then(voteService).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[POST][/search] 해시태그 검색 - 정상 호출, 인증된 사용자")
    @Test
    void search_hashtag() throws Exception {
        // given
        SearchForm searchForm = SearchForm.builder()
                .searchType(SearchType.HASHTAG)
                .searchValue("h")
                .build();

        // when
        mvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("searchForm", searchForm)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("page/search :: #searchResult"))
                .andExpect(model().attributeExists("votes"));

        // then
        then(voteService).should().searchVotes(any(SearchType.class), anyString());
    }

    @DisplayName("[POST][/search] 해시태그 검색 - 인증이 없을 시에는 에러 발생")
    @Test
    void search_hashtag_NoLoginUser() throws Exception {
        // given
        SearchForm searchForm = SearchForm.builder()
                .searchType(SearchType.HASHTAG)
                .searchValue("h")
                .build();

        // when
        mvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("searchForm", searchForm))
                .andDo(print())
                .andExpect(status().isForbidden());

        // then
        then(voteService).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[GET][/timeline] 투표 게시글 조회 - 정상 호출, 인증된 사용자")
    @Test
    void timeLine() throws Exception {
        // given
        UserDto user = UserDto.builder()
                .userId("user")
                .userPassword("password")
                .email("test@email.com")
                .nickname("user-test")
                .memo("test memo")
                .address("서울")
                .build();

        given(voteService.viewTimeLine(eq(Category.ALL), any(Pageable.class))).willReturn(Page.empty());
        given(userService.findUserById(anyString())).willReturn(user);
        given(followService.getFollowingList(anyString())).willReturn(List.of());
        given(followService.getFollowerList(anyString())).willReturn(List.of());

        // when & then
        mvc.perform(get("/timeline")
                        .queryParam("category", Category.ALL.name())
                        .queryParam("sort", "modifiedAt"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("page/timeLine"))
                .andExpect(model().attributeExists("picks"))
                .andExpect(model().attributeExists("votes"))
                .andExpect(model().attributeExists("bookmarks"))
                .andExpect(model().attributeExists("category"))
                .andExpect(model().attributeExists("userId"));
        // then
    }

    @DisplayName("[GET][/timeline] 투표 게시글 조회 - 인증이 없을 시에는 로그인 페이지로 이동")
    @Test
    void timeLine_NoLoginUser() throws Exception {
        // when
        mvc.perform(get("/timeline"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        // then
        then(voteService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("[GET][/timeline-update] 타임라인 업데이트 API - 정상 호출, 인증된 사용자")
    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void timeLineUpdate() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "modifiedAt");
        List<VoteOptionCommentDto> votes = Arrays.asList(new VoteOptionCommentDto(), new VoteOptionCommentDto());
        Page<VoteOptionCommentDto> page = new PageImpl<>(votes, pageable, 10);
        List<Long> bookmarks = Arrays.asList(1L, 2L);

        // when
        when(voteService.viewTimeLine(any(Category.class), any(Pageable.class))).thenReturn(page);
        when(voteService.findBookmarkVoteId(anyString())).thenReturn(bookmarks);

        // then
        MvcResult result = mvc.perform(get("/timeline-update")
                        .param("category", "ALL")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isNotNull();
        assertThat(content).isNotEmpty();

        // response data 검증
        JSONObject jsonObject = new JSONObject(content);
        assertThat(jsonObject.has("bookmarks")).isTrue();
        if (bookmarks.isEmpty()) {
            assertThat(jsonObject.get("bookmarks")).isNull();
        } else {
            assertThat(jsonObject.get("bookmarks")).isInstanceOf(JSONArray.class);
        }
        assertThat(jsonObject.has("votes")).isTrue();
        assertThat(jsonObject.get("votes")).isInstanceOf(JSONArray.class);
        assertThat(jsonObject.has("category")).isTrue();
        assertThat(jsonObject.get("category")).isEqualTo("ALL");
    }

    @DisplayName("[GET][/timeline-update] 타임라인 업데이트 API - 인증이 없을 시에는 로그인 페이지로 이동")
    @Test
    void timeLineUpdate_NoLoginUser() throws Exception {
        // when
        mvc.perform(get("/timeline-update"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        // then
        then(voteService).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[GET][/form] 투표 게시글 생성 페이지 - 정상 호출, 인증된 사용자")
    @Test
    void saveVote() throws Exception {
        // when & then
        mvc.perform(get("/form"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("page/form"));
    }

    @DisplayName("[GET][/form] 투표 게시글 생성 페이지 - 인증이 없을 시에는 로그인 페이지로 이동")
    @Test
    void saveVote_NoLoginUser() throws Exception {
        // when
        mvc.perform(get("/form"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        // then
        then(voteService).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[POST][/form] 투표 게시글 등록 - 정상 호출, 인증된 사용자")
    @Test
    void saveVote_post() throws Exception {
        // given
        VoteForm voteForm = VoteForm.builder()
                .title("new title")
                .content("new content # #hash1 #hash33")
                .category(Category.WORRY)
                .isMultiPick(true)
                .expiredAt(LocalDateTime.now())
                .voteOptions(List.of(VoteOptionFormDto.builder().content("new option1").imageLink("/link/image1.png").build(),
                        VoteOptionFormDto.builder().content("new option2").imageLink("/link/image2.png").build()))
                .build();

        //when
        mvc.perform(post("/form")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("voteForm", voteForm)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/timeline"))
                .andExpect(redirectedUrl("/timeline"));

        // then
        then(voteService).should().saveVote(any(VoteDto.class), Mockito.<VoteOptionDto>anyList(), Mockito.<HashtagDto>anyList());
    }

    @DisplayName("[POST][/form] 투표 게시글 등록 - 인증이 없을 시에는 에러 발생")
    @Test
    void saveVote_post_NoLoginUser() throws Exception {
        // given
        VoteForm voteForm = VoteForm.builder()
                .title("new title")
                .content("new content # #hash1 #hash33")
                .category(Category.WORRY)
                .isMultiPick(true)
                .expiredAt(LocalDateTime.now())
                .voteOptions(List.of(VoteOptionFormDto.builder().content("new option1").imageLink("/link/image1.png").build(),
                        VoteOptionFormDto.builder().content("new option2").imageLink("/link/image2.png").build()))
                .build();

        // when
        mvc.perform(post("/form")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("voteForm", voteForm))
                .andDo(print())
                .andExpect(status().isForbidden());

        // then
        then(voteService).shouldHaveNoInteractions();
    }

    @WithMockUser
    @DisplayName("[GET][/{voteId}/edit] 투표 게시글 수정 페이지 - 정상 호출, 인증된 사용자")
    @Test
    void editVote() throws Exception {
        // given
        Long voteId = 1L;

        UserDto userDto = UserDto.builder()
                .userId("user")
                .userPassword("password")
                .email("email@email.com")
                .nickname("nick")
                .memo("memo")
                .birthday("0101")
                .createdAt(LocalDateTime.now())
                .build();

        VoteDto voteDto = VoteDto.builder()
                .userDto(userDto)
                .title("title")
                .content("new content")
                .category(Category.WORRY)
                .expiredAt(LocalDateTime.now().plusDays(5))
                .createAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .isMultiPick(true)
                .displayRange(DisplayRange.PUBLIC)
                .build();

        given(voteService.getVote(voteId)).willReturn(voteDto);

        // when
        mvc.perform(get("/" + voteId + "/edit"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("page/editForm"))
                .andExpect(model().attribute("voteDto", voteDto));

        // then
        then(voteService).should().getVote(voteId);
    }

    @DisplayName("[GET][/{voteId}/edit] 투표 게시글 수정 페이지 - 인증이 없을 시에는 로그인 페이지로 이동")
    @Test
    void editVote_NoLoginUser() throws Exception {
        // given
        Long voteId = 1L;

        // when
        mvc.perform(get("/" + voteId + "/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        // then
        then(voteService).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[POST][/{voteId}/edit] 투표 게시글 수정 - 정상 호출, 인증된 사용자")
    @Test
    void editVote_post() throws Exception {
        // given
        Long voteId = 1L;
        VoteForm voteForm = VoteForm.builder()
                .title("update title123")
                .content("update content # #hash1")
                .category(Category.FREE)
                .isMultiPick(true)
                .expiredAt(LocalDateTime.now())
                .voteOptions(List.of(VoteOptionFormDto.builder().content("new option1").imageLink("/link/image1.png").build(),
                        VoteOptionFormDto.builder().content("new option2").imageLink("/link/image2.png").build()))
                .build();

        //when
        mvc.perform(post("/" + voteId + "/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("voteForm", voteForm)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/timeline"))
                .andExpect(redirectedUrl("/timeline"));

        // then
        then(voteService).should().updateVote(any(Long.class), any(VoteDto.class), Mockito.<VoteOptionDto>anyList(), Mockito.<HashtagDto>anyList());
    }

    @DisplayName("[POST][/{voteId}/edit] 투표 게시글 수정 - 인증이 없을 시에는 에러 발생")
    @Test
    void editVote_post_NoLoginUser() throws Exception {
        // given
        Long voteId = 1L;
        VoteForm voteForm = VoteForm.builder()
                .title("new title")
                .content("new content # #hash1 #hash33")
                .category(Category.WORRY)
                .isMultiPick(true)
                .expiredAt(LocalDateTime.now())
                .voteOptions(List.of(VoteOptionFormDto.builder().content("new option1").imageLink("/link/image1.png").build(),
                        VoteOptionFormDto.builder().content("new option2").imageLink("/link/image2.png").build()))
                .build();

        // when
        mvc.perform(post("/" + voteId + "/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("voteForm", voteForm))
                .andDo(print())
                .andExpect(status().isForbidden());

        // then
        then(voteService).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[POST][/{voteId}/delete] 투표 게시글 삭제 - 정상 호출, 인증된 사용자")
    @Test
    void deleteVote() throws Exception {
        // given
        Long voteId = 1L;
        String userId = "user";
        willDoNothing().given(voteService).deleteVote(voteId, userId);

        // when
        mvc.perform(post("/" + voteId + "/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/timeline"))
                .andExpect(redirectedUrl("/timeline"));

        // then
        then(voteService).should().deleteVote(voteId, userId);
    }

    @DisplayName("[POST][/{voteId}/delete] 투표 게시글 삭제 - 인증이 없을 시에는 에러 발생")
    @Test
    void deleteVote_NoLoginUser() throws Exception {
        // given
        Long voteId = 1L;

        // when
        mvc.perform(post("/" + voteId + "/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andDo(print())
                .andExpect(status().isForbidden());

        // then
        then(voteService).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[GET][/{voteId}/detail] 투표 상세 페이지 - 정상 호출, 인증된 사용자")
    @Test
    void voteDetail() throws Exception {
        // given
        String userId = "user";
        User user = User.builder()
                .userId(userId)
                .nickname("nickname")
                .build();

        Long voteId = 1L;

        VoteWithOptionDto voteWithOptionDto = VoteWithOptionDto.builder()
                .userDto(UserDto.from(user))
                .build();

        given(voteService.getVoteWithOption(eq(voteId))).willReturn(voteWithOptionDto);
        given(voteCommentService.commentsByVote(eq(voteId), any(Pageable.class))).willReturn(Page.empty());

        // when & then
        mvc.perform(get("/" + voteId + "/detail"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("vote"))
                .andExpect(model().attributeExists("isBookmark"))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("page/voteDetail"));

        then(voteService).should().findBookmarkVoteId(any());
        then(voteService).should().getVoteWithOption(any());
        then(voteCommentService).should().commentsByVote(any(), any());
    }

    @DisplayName("[GET][/{voteId}/detail] 투표 상세 페이지 - 정상 호출, 인증이 없어도 사용 가능")
    @Test
    void voteDetail_NoLoginUser() throws Exception {
        // given
        String userId = "user";
        User user = User.builder()
                .userId(userId)
                .nickname("nickname")
                .build();

        Long voteId = 1L;

        VoteWithOptionDto voteWithOptionDto = VoteWithOptionDto.builder()
                .userDto(UserDto.from(user))
                .build();

        given(voteService.getVoteWithOption(eq(voteId))).willReturn(voteWithOptionDto);
        given(voteCommentService.commentsByVote(eq(voteId), any(Pageable.class))).willReturn(Page.empty());

        // when & then
        mvc.perform(get("/" + voteId + "/detail"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("vote"))
                .andExpect(model().attribute("isBookmark", false))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attribute("user", nullValue()))
                .andExpect(view().name("page/voteDetail"));

        then(voteService).should().getVoteWithOption(any());
        then(voteCommentService).should().commentsByVote(any(), any());
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[GET][/myBookmark] 북마크 조회 페이지 - 정상 호출, 인증된 사용자")
    @Test
    void bookmark() throws Exception {
        String userId = "user";
        given(voteService.viewBookmarks(eq(userId), any(Pageable.class))).willReturn(Page.empty());

        // when & then
        mvc.perform(get("/myBookmark"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("votes"))
                .andExpect(view().name("page/bookmark"));

        then(voteService).should().viewBookmarks(any(), any());
    }

    @DisplayName("[GET][/myBookmark] 북마크 조회 페이지 - 인증이 없을 시에는 로그인 페이지로 이동")
    @Test
    void bookmark_NoLoginUser() throws Exception {
        // given

        // when
        mvc.perform(get("/myBookmark"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        // then
        then(voteService).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[POST][/{voteId}/saveBookmark] 북마크 저장 - 정상 호출, 인증된 사용자")
    @Test
    void saveBookmark() throws Exception {
        // given
        Long voteId = 1L;

        // when & then
        mvc.perform(post("/" + voteId + "/saveBookmark")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(null))
                        .with(csrf()))
                .andExpect(status().isOk());

        then(voteService).should().saveBookmark(any(), any());
    }

    @DisplayName("[POST][/{voteId}/saveBookmark] 북마크 저장 - 인증이 없을 시에는 에러 발생")
    @Test
    void saveBookmark_NoLoginUser() throws Exception {
        // given
        Long voteId = 1L;

        // when
        mvc.perform(post("/" + voteId + "/saveBookmark")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(null)))
                .andDo(print())
                .andExpect(status().isForbidden());

        // then
        then(voteService).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[DELETE][/{voteId}/deleteBookmark] 북마크 삭제 - 정상 호출, 인증된 사용자")
    @Test
    void deleteBookmark() throws Exception {
        // given
        Long voteId = 1L;

        // when & then
        mvc.perform(delete("/" + voteId + "/deleteBookmark")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(null))
                        .with(csrf()))
                .andExpect(status().isOk());

        then(voteService).should().deleteBookmark(any(), any());
    }

    @DisplayName("[DELETE][/{voteId}/deleteBookmark] 북마크 삭제 - 인증이 없을 시에는 에러 발생")
    @Test
    void deleteBookmark_NoLoginUser() throws Exception {
        // given
        Long voteId = 1L;

        // when
        mvc.perform(delete("/" + voteId + "/deleteBookmark")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(null)))
                .andDo(print())
                .andExpect(status().isForbidden());

        // then
        then(voteService).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[DELETE][/deleteAllBookmark] 북마크 전체 삭제 - 정상 호출, 인증된 사용자")
    @Test
    void deleteAllBookmark() throws Exception {
        // when & then
        mvc.perform(delete("/deleteAllBookmark")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(null))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        then(voteService).should().deleteAllBookmark(any());
    }

    @DisplayName("[DELETE][/deleteAllBookmark] 북마크 전체 삭제 - 인증이 없을 시에는 에러 발생")
    @Test
    void deleteAllBookmark_NoLoginUser() throws Exception {
        // given

        // when
        mvc.perform(delete("/deleteAllBookmark")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(null)))
                .andDo(print())
                .andExpect(status().isForbidden());

        // then
        then(voteService).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[GET][/{voteId}/analysis] 투표 분석 결과 페이지 - 정상 호출, 인증된 사용자")
    @Test
    void voteAnalysis() throws Exception {
        // given
        String userId = "user";
        User user = User.builder()
                .userId(userId)
                .nickname("nickname")
                .build();

        Long voteId = 1L;
        VoteWithOptionDto voteWithOptionDto = VoteWithOptionDto.builder()
                .userDto(UserDto.from(user))
                .build();

        given(voteService.getVoteWithOption(eq(voteId))).willReturn(voteWithOptionDto);

        // when & then
        mvc.perform(get("/" + voteId + "/analysis"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("vote"))
                .andExpect(view().name("page/voteAnalyze"));

        then(voteResultService).should().getVoteResults(any());
    }

    @DisplayName("[GET][/{voteId}/analysis] 투표 분석 결과 페이지 - 인증이 없을 시에는 로그인 페이지로 이동")
    @Test
    void voteAnalysis_NoLoginUser() throws Exception {
        // given
        Long voteId = 1L;

        // when
        mvc.perform(get("/" + voteId + "/analysis"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        // then
        then(voteResultService).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[GET][/{voteId}/analysis/csv] 투표 분석 결과 csv 다운로드 - 정상 호출, 인증된 사용자")
    @Test
    void voteAnalysisCSV() throws Exception {
        // given
        Long voteId = 1L;

        // when & then
        mvc.perform(get("/" + voteId + "/analysis/csv")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(null))
                        .with(csrf()))
                .andExpect(status().isOk());

        then(voteResultService).should().getVoteResults(any());
    }

    @DisplayName("[GET][/{voteId}/analysis/csv] 투표 분석 결과 csv 다운로드 - 인증이 없을 시에는 로그인 페이지로 이동")
    @Test
    void voteAnalysisCSV_NoLoginUser() throws Exception {
        // given
        Long voteId = 1L;

        // when
        mvc.perform(get("/" + voteId + "/analysis/csv")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(null)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        // then
        then(voteResultService).shouldHaveNoInteractions();
    }
}