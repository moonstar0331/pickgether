package com.capstone.pick.controller;

import com.capstone.pick.config.CustomUserDetailsService;
import com.capstone.pick.config.TestSecurityConfig;
import com.capstone.pick.controller.request.SignupRequest;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.exeption.DuplicatedUserException;
import com.capstone.pick.service.SmsService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("회원가입 컨트롤러")
@Import(TestSecurityConfig.class)
@WebMvcTest(SignupController.class)
@ComponentScan(basePackages = "com.capstone.pick.config")
@MockBean(JpaMetamodelMappingContext.class)
class SignupControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private SmsService smsService;

    public SignupControllerTest(@Autowired MockMvc mvc, @Autowired ObjectMapper objectMapper) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
    }

    @Test
    @DisplayName("[GET][/signUp] 회원가입 페이지 요청")
    void 회원가입_페이지_요청() throws Exception {
        mvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(view().name("page/signup"));
    }

    @Test
    @DisplayName("[POST][/signup] 회원가입 승인")
    void 회원가입_승인() throws Exception {
        //given
        SignupRequest request = SignupRequest.builder()
                .userId("test")
                .password("test1234!")
                .email("test@email.com")
                .nickname("testNick")
                .memo("memo")
                .gender("male")
                .birthday("1999-04-01")
                .job("student")
                .address("경기")
                .build();

        //when
        mvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"))
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @DisplayName("[POST][/signUp] 회원가입 거절")
    void 회원가입_거절() throws Exception, DuplicatedUserException {

        //given
        SignupRequest request = SignupRequest.builder()
                .userId("test")
                .password("test1234!")
                .email("test@email.com")
                .nickname("testNick")
                .memo("memo")
                .gender("male")
                .birthday("1999-04-01")
                .job("student")
                .address("경기")
                .build();

        doThrow(new DuplicatedUserException()).when(customUserDetailsService).save(any(UserDto.class));

        //when
        mvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept("application/json")
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(view().name("page/signup"));
    }
}

