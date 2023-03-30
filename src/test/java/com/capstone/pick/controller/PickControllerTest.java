package com.capstone.pick.controller;

import com.capstone.pick.config.TestSecurityConfig;
import com.capstone.pick.controller.request.PickRequest;
import com.capstone.pick.service.PickService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("투표 참여 컨트롤러 테스트")
@Import(TestSecurityConfig.class)
@WebMvcTest(PickController.class)
@ComponentScan(basePackages = "com.capstone.pick.config")
@MockBean(JpaMetamodelMappingContext.class)
public class PickControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;

    @MockBean private PickService pickService;

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
}
