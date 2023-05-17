package com.capstone.pick.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf()
                .ignoringAntMatchers("/signup", "/sendSMS")
                .and()
                .rememberMe().and()
                .authorizeRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .antMatchers("/signup", "/sendSMS", "/{voteId}/detail").permitAll()
//                        .antMatchers("/{voteId}/detail").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/timeline")
                .permitAll().and()
                .logout()
                .logoutSuccessUrl("/").and()
                .oauth2Login() // 소셜로그인을 진행하는데
                .loginPage("/login")
                .successHandler(authenticationSuccessHandler) // 인증에 성공하면 핸들러로 이동하여 처리한다
                .userInfoEndpoint() // 사용자 정보를 가져올 때
                .userService(customOAuth2UserService); // 해당 서비스에서 정보를 처리한다

        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
