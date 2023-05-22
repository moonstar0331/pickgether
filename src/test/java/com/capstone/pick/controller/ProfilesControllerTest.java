package com.capstone.pick.controller;

import com.capstone.pick.config.TestSecurityConfig;
import com.capstone.pick.controller.request.EditProfileRequest;
import com.capstone.pick.domain.User;
import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.VoteOption;
import com.capstone.pick.dto.FollowDto;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.dto.VoteOptionCommentDto;
import com.capstone.pick.repository.VoteRepository;
import com.capstone.pick.service.FollowService;
import com.capstone.pick.service.UserService;
import com.capstone.pick.service.VoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("프로필 컨트롤러")
@Import(TestSecurityConfig.class)
@WebMvcTest(ProfilesController.class)
@ComponentScan(basePackages = "com.capstone.pick.config")
@MockBean(JpaMetamodelMappingContext.class)
class ProfilesControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private VoteService voteService;

    @MockBean
    private UserService userService;

    @MockBean
    private FollowService followService;

    @MockBean
    private VoteRepository voteRepository;


    public ProfilesControllerTest(@Autowired MockMvc mvc, @Autowired ObjectMapper objectMapper) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
    }

    @DisplayName("[GET][/profile] 프로필 페이지")
    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void 프로필_뷰_엔드포인트_테스트() throws Exception {
        //given
        UserDto userDto = UserDto.from(User.builder().userId("user").build());
        List<FollowDto> followingList = new ArrayList<>();
        List<FollowDto> followerList = new ArrayList<>();

        //when
        when(userService.findUserById(anyString())).thenReturn(userDto);
        when(followService.getFollowingList(anyString())).thenReturn(followingList);
        when(followService.getFollowerList(anyString())).thenReturn(followerList);

        //then
        mvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attribute("user", userDto))
                .andExpect(model().attribute("accountId", "user"))
                .andExpect(model().attribute("followingCnt", followingList.size()))
                .andExpect(model().attribute("followerCnt", followerList.size()))
                .andExpect(view().name("page/profile"));
    }

    @DisplayName("[GET][/edit-profile] 프로필 편집 페이지 - 정상 호출")
    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void 프로필_편집_뷰_엔드포인트_테스트() throws Exception {
        //given
        UserDto userDto = UserDto.from(User.builder().userId("user").build());

        //when
        when(userService.findUserById(anyString())).thenReturn(userDto);

        mvc.perform(get("/edit-profile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("page/editProfile"));

    }

    @DisplayName("[GET][/profile] 프로필 페이지 - 인증이 없을 시에는 로그인 페이지로 이동")
    @Test
    void profile_noLogin() throws Exception {
        // given

        // when
        mvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        // then
        then(userService).shouldHaveNoInteractions();
    }

    @DisplayName("[POST][/edit-profile] 유저 프로필 수정 - 정상 호출")
    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void editProfile_Post() throws Exception {
        // given
        UserDto userDto = mock(UserDto.class);
        willDoNothing().given(userService).editProfile(eq(userDto), anyString(), anyString(), anyString(), anyString(), anyString());

        // when
        mvc.perform(post("/edit-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(new EditProfileRequest()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/profile"))
                .andExpect(redirectedUrl("/profile"));

        // then
    }

    @DisplayName("[GET][/get-my-vote] 유저가 작성한 투표 게시글을 반환하는 API 엔드포인트 테스트")
    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void testFindMyVote() throws Exception {
        //given
        List<Vote> voteList = new ArrayList<>();
        createVoteList(voteList);

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "modifiedAt"));
        PageImpl<Vote> votePage = new PageImpl<>(voteList, pageable, voteList.size());
        Page<VoteOptionCommentDto> myVote = votePage.map(VoteOptionCommentDto::from);

        //when
        when(voteRepository.findAllByUser_UserId(anyString(), any(Pageable.class))).thenReturn(votePage);
        when(voteService.findMyVote(anyString(), any(Pageable.class))).thenReturn(myVote);

        //then
        MvcResult result = mvc.perform(get("/get-my-vote")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isNotNull();
        assertThat(content).isNotEmpty();

        // response data 검증
        JSONObject jsonObject = new JSONObject(content);
        assertThat(jsonObject.has("myVote")).isTrue();
        if (myVote.isEmpty()) {
            assertThat(jsonObject.get("myVote")).isNull();
        } else {
            assertThat(jsonObject.get("myVote")).isInstanceOf(JSONObject.class);
            // page 객체 검증
            JSONObject myVoteObject = jsonObject.getJSONObject("myVote");
            assertThat(myVoteObject.get("numberOfElements")).isEqualTo(2);
            assertThat(myVoteObject.get("empty")).isEqualTo(false);
            assertThat(myVoteObject.get("first")).isEqualTo(true);
            assertThat(myVoteObject.get("last")).isEqualTo(true);
            assertThat(myVoteObject.get("size")).isEqualTo(5);
            assertThat(myVoteObject.get("totalPages")).isEqualTo(1);
            // 투표 객체 검증
            JSONArray voteArray = myVoteObject.getJSONArray("content");
            for (int i = 0; i < voteArray.length(); i++) {
                JSONObject voteObject = voteArray.getJSONObject(i);
                assertThat(voteObject.get("id")).isEqualTo(voteList.get(i).getId().intValue());
            }
        }
    }

    void createVoteList(List<Vote> voteList) {
        User user = User.builder().userId("testUser").build();

        Vote vote1 = Vote.builder().id(1L).user(user).voteOptions(new ArrayList<>()).voteComments(new ArrayList<>()).build();
        VoteOption voteOption1 = VoteOption.builder().id(1L).vote(vote1).content("vo1").build();
        VoteOption voteOption2 = VoteOption.builder().id(2L).vote(vote1).content("vo2").build();
        vote1.getVoteOptions().add(voteOption1);
        vote1.getVoteOptions().add(voteOption2);

        Vote vote2 = Vote.builder().id(2L).user(user).voteOptions(new ArrayList<>()).voteComments(new ArrayList<>()).build();
        VoteOption voteOption3 = VoteOption.builder().id(3L).vote(vote2).content("vo1").build();
        VoteOption voteOption4 = VoteOption.builder().id(4L).vote(vote2).content("vo2").build();
        vote2.getVoteOptions().add(voteOption3);
        vote2.getVoteOptions().add(voteOption4);

        voteList.add(vote1);
        voteList.add(vote2);
    }
}