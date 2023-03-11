package com.capstone.pick.controller;

import com.capstone.pick.config.TestSecurityConfig;
import com.capstone.pick.controller.form.VoteForm;
import com.capstone.pick.controller.form.VoteOptionFormDto;
import com.capstone.pick.domain.constant.Category;
import com.capstone.pick.dto.HashtagDto;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    // TODO : 타임라인 조회의 경우 추후에 정렬과 페이징이 적용되면 테스트를 수정 해야한다.
    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][GET] 투표 게시글 조회 - 정상 호출")
    @Test
    void timeLine() throws Exception {
        // given

        // when
        mvc.perform(get("/timeline"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("/page/timeLine"))
                .andExpect(model().attributeExists("votes"));

        // then
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 새 투표 게시글 등록 - 정상 호출")
    @Test
    void saveVote() throws Exception {
        // given
        VoteForm voteForm = VoteForm.builder()
                .title("new title")
                .content("new content # #hash1 #hash33")
                .category(Category.WORRY)
                .isMultiPick(true)
                .expiredAt(LocalDateTime.now())
                .voteOptions(List.of(VoteOptionFormDto.builder().content("new option1").imageLink("/link/image1.png").build(),
                        VoteOptionFormDto.builder().content("new option2").imageLink("/link/image2.png").build()))
                .build();

        //when
        mvc.perform(post("/form")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("voteForm", voteForm)
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/timeLine"))
                .andExpect(redirectedUrl("/timeLine"));

        // then
        then(voteService).should().saveVote(any(VoteDto.class), Mockito.<VoteOptionDto>anyList(), Mockito.<HashtagDto>anyList());
    }

    @WithUserDetails(value = "user", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 투표 게시글 수정 - 정상 호출")
    @Test
    void updateVote() throws Exception {
        // given
        long voteId = 1L;

        VoteForm voteForm = VoteForm.builder()
                .title("update title123")
                .content("update content # #hash1")
                .category(Category.FREE)
                .isMultiPick(true)
                .expiredAt(LocalDateTime.now())
                .voteOptions(List.of(VoteOptionFormDto.builder().content("new option1").imageLink("/link/image1.png").build(),
                        VoteOptionFormDto.builder().content("new option2").imageLink("/link/image2.png").build()))
                .build();

        //when
        mvc.perform(post("/" + voteId + "/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("voteForm", voteForm)
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/timeLine"))
                .andExpect(redirectedUrl("/timeLine"));

        // then
        then(voteService).should().updateVote(any(Long.class), any(VoteDto.class), Mockito.<VoteOptionDto>anyList(), Mockito.<HashtagDto>anyList());
    }
}