package com.capstone.pick.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf()
                .disable()
                .authorizeHttpRequests()
                .anyRequest()
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/page/login.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/page/timeLine.html", true)
                .failureUrl("/login.html?error=true")
                .and()
                .build();
    }
}
