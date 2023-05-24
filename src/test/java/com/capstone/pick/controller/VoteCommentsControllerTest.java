package com.capstone.pick.controller;

import com.capstone.pick.config.TestSecurityConfig;
import com.capstone.pick.controller.request.EditProfileRequest;
import com.capstone.pick.controller.request.LikeRequest;
import com.capstone.pick.controller.request.PostCommentRequest;
import com.capstone.pick.dto.CommentDto;
import com.capstone.pick.repository.CommentLikeRepository;
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
    @Autowired
    private CommentLikeRepository commentLikeRepository;

    public VoteCommentsControllerTest(@Autowired MockMvc mvc, @Autowired ObjectMapper objectMapper) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[GET][/{voteId}/comments] 댓글 상세보기 페이지 - 정상 호출, 인증된 사용자")
    @Test
    void comments() throws Exception {
        // given
        Long voteId = 1L;
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

    @DisplayName("[GET][/{voteId}/comments] 댓글 상세보기 페이지 - 인증이 없을 시에는 로그인 페이지로 이동")
    @Test
    void comments_NoLoginUser() throws Exception {
        // given
        Long voteId = 1L;

        // when
        mvc.perform(get("/" + voteId + "/comments"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        // then
        then(voteCommentService).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[POST][/{voteId}/comments] 댓글 저장 - 정상 호출, 인증된 사용자")
    @Test
    void saveVoteComment() throws Exception {
        // given
        Long voteId = 1L;

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

    @DisplayName("[POST][/{voteId}/comments] 댓글 저장 - 인증이 없을 시에는 에러 발생")
    @Test
    void saveComment_NoLoginUser() throws Exception {
        // given
        Long voteId = 1L;

        // when
        mvc.perform(post("/" + voteId + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(new EditProfileRequest())))
                .andDo(print())
                .andExpect(status().isForbidden());

        // then
        then(voteCommentService).shouldHaveNoInteractions();
        then(commentLikeRepository).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[PUT][/{voteId}/comments/{commentId}] 댓글 수정 - 정상 호출, 인증된 사용자")
    @Test
    void updateComment() throws Exception {
        // given
        Long voteId = 1L;
        Long commentId = 1L;

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

    @DisplayName("[PUT][/{voteId}/comments/{commentId}] 댓글 수정 - 인증이 없을 시에는 에러 발생")
    @Test
    void updatecomment_NoLoginUser() throws Exception {
        // given
        Long voteId = 1L;
        Long commentId = 1L;

        // when
        mvc.perform(put("/" + voteId + "/comments/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(new PostCommentRequest())))
                .andDo(print())
                .andExpect(status().isForbidden());

        // then
        then(voteCommentService).shouldHaveNoInteractions();
        then(commentLikeRepository).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[DELETE][/{voteId}/comments/{commentId}] 댓글 삭제 - 정상 호출, 인증된 사용자")
    @Test
    void deleteComment() throws Exception {
        // given
        Long voteId = 1L;
        Long commentId = 1L;
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

    @DisplayName("[DELETE][/{voteId}/comments/{commentId}] 댓글 삭제 - 인증이 없을 시에는 에러 발생")
    @Test
    void deleteComment_NoLoginUser() throws Exception {
        // given
        Long voteId = 1L;
        Long commentId = 1L;

        // when
        mvc.perform(delete("/" + voteId + "/comments/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(null)))
                .andDo(print())
                .andExpect(status().isForbidden());

        // then
        then(voteCommentService).shouldHaveNoInteractions();
        then(commentLikeRepository).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[POST][/like] 댓글 좋아요 저장 - 정상 호출, 인증된 사용자")
    @Test
    void saveLike() throws Exception {
        // given
        Long voteId = 1L;
        Long commentId = 1L;

        // when & then
        mvc.perform(post("/like")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(new LikeRequest(voteId, commentId)))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @DisplayName("[POST][/like] 댓글 좋아요 저장 - 인증이 없을 시에는 에러 발생")
    @Test
    void saveLike_NoLoginUser() throws Exception {
        // given
        Long voteId = 1L;
        Long commentId = 1L;

        // when
        mvc.perform(post("/like")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(new LikeRequest(voteId, commentId))))
                .andDo(print())
                .andExpect(status().isForbidden());

        // then
        then(voteCommentService).shouldHaveNoInteractions();
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[DELETE][/like] 댓글 좋아요 삭제 - 정상 호출, 인증된 사용자")
    @Test
    void deleteLike() throws Exception {
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

    @DisplayName("[DELETE][/like] 댓글 좋아요 삭제 - 인증이 없을 시에는 에러 발생")
    @Test
    void deleteLike_NoLoginUser() throws Exception {
        // given
        Long voteId = 1L;
        Long commentId = 1L;

        // when
        mvc.perform(delete("/like")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(new LikeRequest(voteId, commentId))))
                .andDo(print())
                .andExpect(status().isForbidden());

        // then
        then(voteCommentService).shouldHaveNoInteractions();
    }
}