package com.capstone.pick.controller;

import com.capstone.pick.config.TestSecurityConfig;
import com.capstone.pick.dto.VoteDto;
import com.capstone.pick.dto.VoteOptionDto;
import com.capstone.pick.service.VoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 투표 게시글")
@Import(TestSecurityConfig.class)
@WebMvcTest(VoteController.class)
class VoteControllerTest {

    private final MockMvc mvc;

    @MockBean
    private VoteService voteService;

    public VoteControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 새 투표 게시글 등록 - 정상 호출")
    @Test
    void saveVote() throws Exception {
        // given

        //when
        mvc.perform(post("/form")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "new title")
                        .param("content", "new content")
                        .param("category", "WORRY")
                        .param("isMultiPick", "true")
                        .param("expiredAt", "2023-03-31T02:35")
                        .param("displayRange", "PUBLIC")
                        .param("voteOptions[0].content", "test option1")
                        .param("voteOptions[1].content", "test option2")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/timeLine"))
                .andExpect(redirectedUrl("/timeLine"));

        // then
        then(voteService).should().saveVote(any(VoteDto.class), Mockito.<VoteOptionDto>anyList());
    }
}