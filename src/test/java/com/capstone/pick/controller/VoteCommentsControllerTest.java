package com.capstone.pick.controller;

import com.capstone.pick.config.TestSecurityConfig;
import com.capstone.pick.controller.form.CommentForm;
import com.capstone.pick.dto.CommentDto;
import com.capstone.pick.service.VoteCommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
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
@WithMockUser
class VoteCommentsControllerTest {

    private final MockMvc mvc;

    @MockBean
    private VoteCommentService voteCommentService;

    public VoteCommentsControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[Comment][GET][/comments] 댓글 상세보기 페이지")
    @Test
    void 댓글상세보기_뷰_엔드포인트_테스트() throws Exception {
        mvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("page/comments"));
    }


    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[comment][POST][/comments/new")
    @Test
    void saveVoteComment() throws Exception {

        // given
        long voteId = 1L;
        CommentForm commentForm = CommentForm.builder()
                .voteId(voteId)
                .content("new comment")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        willDoNothing().given(voteCommentService).saveComment(any(CommentDto.class));

        // when
        mvc.perform(post("/comments/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("commentForm", commentForm)
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection());
//                .andExpect(view().name("page/voteDetail"))
//                .andExpect(redirectedUrl("/timeLine/{voteId}"));
        /**
         * TODO: 테스트 구현 시에 아직은 댓글 출력 화면에 대한 의논을 나누지 않았기 때문에
         *       view 와 관련된 테스트는 주석으로 남겨둔다.
          */

        // then
        then(voteCommentService).should().saveComment(any(CommentDto.class));
    }
}