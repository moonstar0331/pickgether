package com.capstone.pick.controller;

import com.capstone.pick.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 댓글")
@Import(SecurityConfig.class)
@WebMvcTest(VoteCommentsController.class)
class VoteCommentsControllerTest {

    private final MockMvc mvc;

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
}