package com.capstone.pick.controller;

import com.capstone.pick.config.TestSecurityConfig;
import com.capstone.pick.controller.form.CommentForm;
import com.capstone.pick.domain.constant.OrderCriteria;
import com.capstone.pick.dto.CommentDto;
import com.capstone.pick.dto.CommentLikeDto;
import com.capstone.pick.service.VoteCommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 댓글")
@Import(TestSecurityConfig.class)
@WebMvcTest(VoteCommentsController.class)
@ComponentScan(basePackages = "com.capstone.pick.config")
@MockBean(JpaMetamodelMappingContext.class)
class VoteCommentsControllerTest {

    private final MockMvc mvc;

    @MockBean
    private VoteCommentService voteCommentService;

    public VoteCommentsControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
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

        // when & then
        mvc.perform(get("/" + voteId + "/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("userId"))
                .andExpect(model().attributeExists("commentPosts"))
                .andExpect(view().name("page/comments"));
    }


    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[comment][POST][/{voteId}/comments")
    @Test
    void saveVoteComment() throws Exception {
        // given
        long voteId = 1L;
        CommentForm commentForm = createCommentForm(voteId);

        willDoNothing().given(voteCommentService).saveComment(any(CommentDto.class));

        // when
        mvc.perform(post("/" + voteId + "/comments").param("orderBy", OrderCriteria.LATEST.name())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("commentForm", commentForm)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/" + voteId + "/comments"))
                .andExpect(redirectedUrl("/" + voteId + "/comments"));

        // then
        then(voteCommentService).should().saveComment(any(CommentDto.class));
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[comment][POST][/{voteId}/comments/{commentId}]")
    @Test
    void updateComment() throws Exception {
        // given
        long voteId = 1L;
        long commentId = 1L;

        CommentForm commentForm = CommentForm.builder()
                .voteId(voteId)
                .content("new content")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        // when
        mvc.perform(post("/" + voteId + "/comments/" + commentId + "/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("commentForm", commentForm)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/" + voteId + "/comments"))
                .andExpect(redirectedUrl("/" + voteId + "/comments"));

        // then
        then(voteCommentService).should().updateComment(any(Long.class), any(CommentDto.class));
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[comment][DELETE][/{voteId}/comments/{commentId}]")
    @Test
    void deleteComment() throws Exception {
        // given
        long voteId = 1L;
        long commentId = 1L;
        CommentForm commentForm = createCommentForm(voteId);

        // when
        mvc.perform(post("/" + voteId + "/comments/" + commentId + "/delete")
                        .with(csrf()))
                .andExpect(view().name("redirect:/" + voteId + "/comments"))
                .andExpect(redirectedUrl("/" + voteId + "/comments"));

        // then
        then(voteCommentService).should().deleteComment(any(Long.class), any(String.class));
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[commentLike][POST][/{voteId}/comments/{commentId}/like]")
    @Test
    void saveCommentLike() throws Exception {
        // given
        long voteId = 1L;
        long commentId = 1L;

        willDoNothing().given(voteCommentService).saveCommentLike(any(CommentLikeDto.class));

        // when
        mvc.perform(post("/" + voteId + "/comments/" + commentId + "/like")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/" + voteId + "/comments"))
                .andExpect(redirectedUrl("/" + voteId + "/comments"));

        // then
        then(voteCommentService).should().saveCommentLike(any(CommentLikeDto.class));
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[commentLike][DELETE][/{voteId}/comments/{likeId}/deleteLike]")
    @Test
    void deleteCommentLike() throws Exception {
        // given
        long voteId = 1L;
        long likeId = 1L;

        // when
        mvc.perform(post("/" + voteId + "/comments/" + likeId + "/deleteLike")
                        .with(csrf()))
                .andExpect(view().name("redirect:/" + voteId + "/comments"))
                .andExpect(redirectedUrl("/" + voteId + "/comments"));

        // then
        then(voteCommentService).should().deleteCommentLike(any(Long.class), any(String.class));
    }

    private static CommentForm createCommentForm(long voteId) {
        return CommentForm.builder()
                .voteId(voteId)
                .content("new comment")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}