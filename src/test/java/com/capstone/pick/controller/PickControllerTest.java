package com.capstone.pick.controller;

import com.capstone.pick.config.TestSecurityConfig;
import com.capstone.pick.controller.request.PickRequest;
import com.capstone.pick.domain.Follow;
import com.capstone.pick.domain.User;
import com.capstone.pick.dto.FollowDto;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.security.VotePrincipal;
import com.capstone.pick.service.FollowService;
import com.capstone.pick.service.PickService;
import com.capstone.pick.service.VoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("투표 참여 컨트롤러 테스트")
@Import(TestSecurityConfig.class)
@WebMvcTest(PickController.class)
@ComponentScan(basePackages = "com.capstone.pick.config")
@MockBean(JpaMetamodelMappingContext.class)
public class PickControllerTest {

    @InjectMocks
    private PickController pickController;

    @MockBean private PickService pickService;

    @MockBean private FollowService followService;

    @MockBean private VoteService voteService;

    @MockBean private VotePrincipal votePrincipal;
    private final ObjectMapper objectMapper;
    private final MockMvc mvc;

    public PickControllerTest(@Autowired MockMvc mvc, @Autowired ObjectMapper objectMapper) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[pick][POST] 투표 참여 - 정상 호출")
    @Test
    void pick() throws Exception {

        PickRequest pickRequest = PickRequest.builder()
                .optionId(1L)
                .build();

        mvc.perform(post("/pick")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(pickRequest))
                        .with(csrf())
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @WithAnonymousUser
    @DisplayName("[pick][POST] 투표 참여 - 인증없는 유저이면 에러 발생")
    @Test
    void pick_NoLoginUser() throws Exception {

        PickRequest pickRequest = PickRequest.builder()
                .optionId(1L)
                .build();

        mvc.perform(post("/pick")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(pickRequest))
                ).andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("[GET][/{voteId}/participants] 참여자 리스트 페이지 테스트")
    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void 참여자_리스트_뷰_엔드포인트_테스트() throws Exception {
        //given
        Long voteId = 1L;
        User user = User.builder().userId("user").build();
        List<FollowDto> followingList = createFollowList(user);
        List<String> followingUserIdList = createFollowList(user).stream()
                .map(FollowDto::getToUser).map(UserDto::getUserId).collect(Collectors.toList());

        Page<UserDto> participants = new PageImpl<>(Arrays.asList(
                UserDto.from(User.builder().userId("participants1").build()),
                UserDto.from(User.builder().userId("participants3").build()),
                UserDto.from(User.builder().userId("participants2").build())
        ));

        //when
        when(votePrincipal.getUsername()).thenReturn("user");
        when(pickService.getParticipants(eq(voteId), any(Pageable.class), eq(followingUserIdList))).thenReturn(participants);
        when(followService.getFollowingList("user")).thenReturn(followingList);

        //then
        mvc.perform(get("/" + voteId + "/participants"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("participants", participants))
                .andExpect(model().attribute("followingUserIdList", followingUserIdList))
                .andExpect(model().attribute("maxCnt", 6))
                .andExpect(view().name("page/participants"));
    }

    @Test
    @DisplayName("[GET][/{voteId}/json-participant] 참여자 리스트 API 엔드포인트 테스트")
    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void testParticipantJson() throws Exception {
        // given
        String username = "testUser";
        Long voteId = 5L;
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<String> followingUserIdList = Arrays.asList("following1", "following2");
        List<UserDto> userDtos = Arrays.asList(
                UserDto.builder().userId("test1").build(),
                UserDto.builder().userId("test2").build(),
                UserDto.builder().userId("test3").build());
        Page<UserDto> participants = new PageImpl<>(userDtos, pageRequest, userDtos.size());
        Long pickCount = 5L;
        // when
        when(followService.getFollowingList(username)).thenReturn(Arrays.asList(new FollowDto()));
        when(pickService.getParticipants(anyLong(), any(), anyList()))
                .thenReturn(participants);
        when(voteService.getPickCount(voteId)).thenReturn(pickCount);

        // then
        MvcResult result = mvc.perform(get("/" + voteId + "/json-participants")
                        .principal(new UsernamePasswordAuthenticationToken(username, ""))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).isNotNull();
        assertThat(content).isNotEmpty();

        // response data 검증
        JSONObject jsonObject = new JSONObject(content);
        assertThat(jsonObject.has("participants")).isTrue();
        if (participants.isEmpty()) { // participants가 비어있는 경우
            assertThat(jsonObject.get("participants")).isNull();
        } else {
            assertThat(jsonObject.get("participants")).isInstanceOf(JSONObject.class);
        }
        assertThat(jsonObject.has("followingUserIdList")).isTrue();
        assertThat(jsonObject.get("followingUserIdList")).isInstanceOf(JSONArray.class);
        assertThat(jsonObject.has("voteId")).isTrue();
        assertThat(jsonObject.get("voteId")).isEqualTo(voteId.intValue());
        assertThat(jsonObject.has("maxCnt")).isTrue();
        assertThat(jsonObject.get("maxCnt")).isEqualTo(6);
        assertThat(jsonObject.has("size")).isTrue();
        assertThat(jsonObject.get("size")).isEqualTo(pickCount.intValue());
    }

    private static Follow createFollow(Long id, User fromUser, User toUser) {
        return Follow.builder()
                .id(id)
                .fromUser(fromUser)
                .toUser(toUser)
                .build();
    }

    private static List<FollowDto> createFollowList(User user) {
        User toUser1 = User.builder().userId("participants1").build();
        User toUser2 = User.builder().userId("participants3").build();
        Follow follow1 = createFollow(1L, user, toUser1);
        Follow follow2 = createFollow(2L, user, toUser2);
        List<Follow> followingList = new ArrayList<>();
        followingList.add(follow1);
        followingList.add(follow2);
        return followingList.stream().map(FollowDto::from).collect(Collectors.toList());
    }
}