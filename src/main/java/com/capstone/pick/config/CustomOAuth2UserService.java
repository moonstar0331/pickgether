package com.capstone.pick.config;

import com.capstone.pick.domain.User;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.repository.UserRepository;
import com.capstone.pick.security.VotePrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final PasswordEncoder passwordEncoder;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);

        // 어떤 소셜로그인을 이용하는지 구분 하기위해 쓰임.
        // ex) registrationId = "naver", registrationId = "google" 등등
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();


        // OAuth2 로그인 시 키 값이 된다.
        // 구글은 키 값이 "sub"이고, 네이버는 "response"이고, 카카오는 "id"이다.
        // 각각 다르므로 이렇게 따로 변수로 받아서 넣어줘야함.
        String userNameAttributeName = oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2 로그인을 통해 가져온 OAuth2User의 attribute를 담아주는 of 메소드.
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        String encode = passwordEncoder.encode(UUID.randomUUID().toString());


        VotePrincipal votePrincipal = VotePrincipal.builder()
                .username(registrationId + "_" + attributes.getUsername())
                .password(encode)
                .email(attributes.getEmail())
                .nickname(attributes.getName())
                .birthday(attributes.getBirthday())
                .memo("임시 값")
                .provider(registrationId)
                .gender(attributes.getGender())
                .age_range(attributes.getAge_range())
                .oAuthAttributes(attributes)
                .build();


        return votePrincipal;

    }

}