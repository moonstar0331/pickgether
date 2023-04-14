package com.capstone.pick.controller;

import com.capstone.pick.config.TestSecurityConfig;
import com.capstone.pick.controller.request.LikeRequest;
import com.capstone.pick.controller.request.PostCommentRequest;
import com.capstone.pick.dto.CommentDto;
import com.capstone.pick.service.VoteCommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 댓글")
@Import(TestSecurityConfig.class)
@WebMvcTest(VoteCommentsController.class)
@ComponentScan(basePackages = "com.capstone.pick.config")
@MockBean(JpaMetamodelMappingContext.class)
class VoteCommentsControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private VoteCommentService voteCommentService;

    public VoteCommentsControllerTest(@Autowired MockMvc mvc, @Autowired ObjectMapper objectMapper) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
    }

    @DisplayName("[Comment][GET][/{voteId}/comments] 댓글 상세보기 페이지 - 인증이 없을 땐 로그인 페이지로 이동")
    @Test
    void noLoginUser_readComments() throws Exception {
        // given
        long voteId = 1L;

        // when
        mvc.perform(get("/" + voteId + "/comments"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        // then
        then(voteCommentService).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[Comment][GET][/{voteId}/comments] 댓글 상세보기 페이지 - 인증된 사용자")
    @Test
    void 댓글상세보기_뷰_엔드포인트_테스트() throws Exception {
        // given
        long voteId = 1L;
        given(voteCommentService.commentsByVote(eq(voteId), any(Pageable.class))).willReturn(Page.empty());

        // when & then
        mvc.perform(get("/" + voteId + "/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("page/comments"));

        then(voteCommentService).should().commentsByVote(any(), any());
    }


    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[comment][POST][/{voteId}/comments")
    @Test
    void saveVoteComment() throws Exception {
        // given
        long voteId = 1L;

        willDoNothing().given(voteCommentService).saveComment(any(CommentDto.class));
        given(voteCommentService.commentsByVote(eq(voteId), any(Pageable.class))).willReturn(Page.empty());

        // when
        mvc.perform(post("/" + voteId + "/comments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(new PostCommentRequest()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("page/voteDetail :: #commentList"))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attributeExists("user"));

        // then
        then(voteCommentService).should().saveComment(any(CommentDto.class));
        then(voteCommentService).should().commentsByVote(anyLong(), any(Pageable.class));
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[comment][PUT][/{voteId}/comments/{commentId}]")
    @Test
    void updateComment() throws Exception {
        // given
        long voteId = 1L;
        long commentId = 1L;

        willDoNothing().given(voteCommentService).updateComment(eq(commentId), any(CommentDto.class));
        given(voteCommentService.commentsByVote(eq(voteId), any(Pageable.class))).willReturn(Page.empty());

        // when
        mvc.perform(put("/" + voteId + "/comments/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(new PostCommentRequest()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("page/voteDetail :: #commentList"))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attributeExists("user"));

        // then
        then(voteCommentService).should().updateComment(anyLong(), any(CommentDto.class));
        then(voteCommentService).should().commentsByVote(anyLong(), any(Pageable.class));
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[comment][DELETE][/{voteId}/comments/{commentId}]")
    @Test
    void deleteComment() throws Exception {
        // given
        long voteId = 1L;
        long commentId = 1L;
        String userId = "user";

        willDoNothing().given(voteCommentService).deleteComment(eq(commentId), eq(userId));
        given(voteCommentService.commentsByVote(eq(voteId), any(Pageable.class))).willReturn(Page.empty());

        // when
        mvc.perform(delete("/" + voteId + "/comments/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(null))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("page/voteDetail :: #commentList"))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attributeExists("user"));

        // then
        then(voteCommentService).should().deleteComment(anyLong(), anyString());
        then(voteCommentService).should().commentsByVote(anyLong(), any(Pageable.class));
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[commentLike][POST][/like]")
    @Test
    void saveCommentLike() throws Exception {
        // given
        long voteId = 1L;
        long commentId = 1L;

        // when & then
        mvc.perform(post("/like")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(new LikeRequest(voteId, commentId)))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[commentLike][DELETE][/like]")
    @Test
    void deleteCommentLike() throws Exception {
        // given
        Long voteId = 1L;
        Long commentId = 1L;

        // when & then
        mvc.perform(delete("/like")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(new LikeRequest(voteId, commentId)))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}