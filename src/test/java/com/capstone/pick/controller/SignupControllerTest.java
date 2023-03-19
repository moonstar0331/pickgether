package com.capstone.pick.controller;

import com.capstone.pick.config.TestSecurityConfig;
import com.capstone.pick.controller.form.SignUpForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    public SignupControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    @DisplayName("[GET][/signUp] 회원가입 페이지 요청")
    void 회원가입_페이지_요청() throws Exception {
        mvc.perform(get("/signUp"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(view().name("/page/signUp"));
    }

    @Test
    @DisplayName("[POST][/signUp] 회원가입 승인")
    void 회원가입_승인() throws Exception {

        //given
        SignUpForm form = SignUpForm.builder()
                .userId("testUser")
                .userPassword("testPWD")
                .email("test@gmail.com")
                .build();

        //when
        mvc.perform(post("/signUp")
                .flashAttr("signUpForm",form)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @DisplayName("[POST][/signUp] 회원가입 거절")
    void 회원가입_거절() throws Exception {

        //given
        SignUpForm failForm = SignUpForm.builder()
                .userId("user")
                .userPassword("testPWD")
                .email("testmail@gmail.com")
                .build();

        //when
        mvc.perform(post("/signUp")
                .flashAttr("signUpForm",failForm)
                .with(csrf()))
                .andExpect(status().is4xxClientError()); // 핸들러에서 409 에러로 처리

    }


}

