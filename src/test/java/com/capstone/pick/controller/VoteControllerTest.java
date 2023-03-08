package com.capstone.pick.controller;

import com.capstone.pick.config.TestSecurityConfig;
import com.capstone.pick.controller.form.VoteForm;
import com.capstone.pick.controller.form.VoteOptionFormDto;
import com.capstone.pick.dto.VoteDto;
import com.capstone.pick.dto.VoteOptionDto;
import com.capstone.pick.service.VoteService;
import com.capstone.pick.util.FormDataEncoder;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 투표 게시글")
@Import({TestSecurityConfig.class, FormDataEncoder.class})
@WebMvcTest(VoteController.class)
class VoteControllerTest {

    private final MockMvc mvc;
    private final FormDataEncoder formDataEncoder;

    @MockBean
    private VoteService voteService;

    public VoteControllerTest(@Autowired MockMvc mvc, @Autowired FormDataEncoder formDataEncoder) {
        this.mvc = mvc;
        this.formDataEncoder = formDataEncoder;
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 새 투표 게시글 등록 - 정상 호출")
    @Test
    void saveVote() throws Exception {
        // given
        VoteForm voteForm = VoteForm.builder()
                .title("new title")
                .content("new content")
                .voteOptions(List.of(
                        VoteOptionFormDto.builder().content("new option1").imageLink("/link/image1.png").build(),
                        VoteOptionFormDto.builder().content("new option2").imageLink("/link/image2.png").build()))
                .build();

        willDoNothing().given(voteService).saveVote(any(VoteDto.class), Mockito.<VoteOptionDto>anyList());

        // when
        mvc.perform(post("/form").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(formDataEncoder.encode(voteForm))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(redirectedUrl("/"));

        // then
        then(voteService).should().saveVote(any(VoteDto.class), Mockito.<VoteOptionDto>anyList());
    }
}