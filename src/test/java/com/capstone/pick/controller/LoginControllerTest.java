package com.capstone.pick.controller;

import com.capstone.pick.config.CustomOAuth2UserService;
import com.capstone.pick.config.TestSecurityConfig;
import com.capstone.pick.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DisplayName("로그인 컨트롤러")
@Import(TestSecurityConfig.class)
@WebMvcTest(LoginController.class)
@ComponentScan(basePackages = "com.capstone.pick.config")
@MockBeans({
        @MockBean(JpaMetamodelMappingContext.class),
        @MockBean(UserService.class)
})
class LoginControllerTest {

    private final MockMvc mvc;

    public LoginControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[GET][/] 메인 페이지")
    @Test
    public void 로그인_뷰_엔드포인트_테스트() throws Exception {

        // When & Then
        mvc.perform(get("/login"))
                .andExpect(status().isOk()) // 상태코드가 200인가?
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // html 파일을 리턴해주는가?
                .andExpect(view().name("page/login")); // 리턴하는 뷰 이름은 무엇인가?
                //.andExpect(model().attributeExists("")); // 뷰에 애트리뷰트가 존재하는가?
    }

    @DisplayName("[GET][/oauth2/authorization/] 소셜로그인 시도")
    @Test
    public void 소셜로그인_테스트() throws Exception {

        // When & Then
        mvc.perform(get("/oauth2/authorization/kakao")) // 카카오 로그인 버튼을 클릭했을때
                .andExpect(status().is3xxRedirection()) // 카카오 로그인 페이지로 이동시키는지?  (정확한 redirection url 지정 불가능)
                .andDo(MockMvcResultHandlers.print());

        mvc.perform(get("/oauth2/authorization/naver")) // 네이버 로그인 버튼을 클릭했을때
                .andExpect(status().is3xxRedirection()) // 네이버 로그인 페이지로 이동시키는지?
                .andDo(MockMvcResultHandlers.print());

        mvc.perform(get("/oauth2/authorization/google")) // 구글 로그인 버튼을 클릭했을때
                .andExpect(status().is3xxRedirection()) // 구글 로그인 페이지로 이동시키는지?
                .andDo(MockMvcResultHandlers.print());

    }

//    @DisplayName("[GET][/addMoreInfo] 추가정보 입력페이지 요청")
//    @Test
//    public void 추가정보_입력_테스트() throws Exception {
//
//        // When & Then
//        mvc.perform(get("/addMoreInfo").with(oauth2Login()
//                // 1
//                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
//                // 2
//                .attributes(attributes -> {
//                    attributes.put("username", "username");
//                    attributes.put("name", "name");
//                    attributes.put("email", "my@email");
//                    attributes.put("picture", "https://my_picture");
//                })
//        ))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
//                .andExpect(view().name("page/addMoreInfo"))
//                .andExpect(model().attributeExists("addMoreInfo"));
//    }

}