package com.capstone.pick.config;


import com.capstone.pick.domain.User;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.repository.UserRepository;
import com.capstone.pick.security.VotePrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {


    private final UserRepository userRepository;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        // 인증 정보가 일반 로그인 이던지 소셜로그인 이던지에 상관없이 VotePrincipal 로 치환가능
        // VotePrincipal 은 OAuth2User,UserDetails 을 구현하고 있기 때문
        VotePrincipal votePrincipal = (VotePrincipal) authentication.getPrincipal();

        if(isEmpty(votePrincipal.toDto())){ // 기존에 저장된 유저정보가 없다면 저장시키고 2차 정보 입력페이지로 이동

            userRepository.save(votePrincipal.toDto().toEntity());
            redirectStrategy.sendRedirect(request, response, "/addMoreInfo");

        }else { // 기존에 저장된 유저정보가 있다면 타임라인으로 이동

            redirectStrategy.sendRedirect(request, response, "/timeline");

        }

    }

    // 저장된 유저정보가 있는지 판단
    private boolean isEmpty(UserDto userDto) {

        Optional<User> user = userRepository.findById(userDto.getUserId());
        return user.isEmpty() ? true: false;
    }
}
