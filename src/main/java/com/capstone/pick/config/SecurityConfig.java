package com.capstone.pick.config;

import com.capstone.pick.dto.UserDto;
import com.capstone.pick.repository.UserRepository;
import com.capstone.pick.security.VotePrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.httpBasic().disable()
                .csrf()
                .and()
                    .rememberMe()
                .and()
                    .authorizeRequests(auth -> auth
                            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                            .antMatchers("/login").permitAll()
                            .anyRequest().authenticated()
                    )
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/timeline")
                    .permitAll()
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                .and()
                    .oauth2Login() // 소셜로그인을 진행하는데
                    .loginPage("/login")
                    .defaultSuccessUrl("/timeline")
                    //.successHandler(authenticationSuccessHandler) //성공하면 커스텀한 핸들러에서 처리한다
                    .userInfoEndpoint() // 사용자 정보를 가져올 때
                    .userService(customOAuth2UserService); // 커스텀한 서비스에서 정보를 처리한다
        
        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
