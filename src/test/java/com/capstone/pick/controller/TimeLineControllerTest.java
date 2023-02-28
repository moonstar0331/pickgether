package com.capstone.pick.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("타임라인 컨트롤러")
@WebMvcTest(TimeLineController.class)
@WithMockUser
class TimeLineControllerTest {

    private final MockMvc mvc;

    public TimeLineControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[TimeLine][GET][/timeline] 타임라인 페이지")
    @Test
    public void 타임라인_뷰_엔드포인트_테스트() throws Exception {

        // When & Then
        mvc.perform(MockMvcRequestBuilders.get("/timeline"))
                .andExpect(status().isOk()) // 상태코드가 200인가?
                .andExpect(result -> content().contentType(MediaType.TEXT_HTML)) // html 파일을 리턴해주는가?
                .andExpect(view().name("/page/timeLine")); // 리턴하는 뷰 이름은 무엇인가?
        //.andExpect(model().attributeExists("")); // 뷰에 애트리뷰트가 존재하는가?
    }

}