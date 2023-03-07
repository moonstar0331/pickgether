package com.capstone.pick.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

//    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http.csrf()
//                .disable()
//                .authorizeHttpRequests()
//                .anyRequest()
//                .permitAll()
//                .and()
//                .formLogin()
//                .loginPage("/page/login.html")
//                .loginProcessingUrl("/login")
//                .defaultSuccessUrl("/page/timeLine.html", true)
//                .failureUrl("/login.html?error=true")
//                .and()
//                .build();

        return http.httpBasic().disable()
                .csrf().and()
                .rememberMe().and()
                .authorizeRequests()
                .anyRequest().permitAll().and()
                .formLogin()
                .loginPage("/page/login.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/page/timeLine.html", true)
                .failureUrl("/login.html?error=true").permitAll().and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/page/login.html")
                .and()
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

//    @Bean
//    protected UserDetailsService userDetailsService() {
//        return username -> {
//            User user = userService.findByUsername(username);
//            if(user == null) throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
//            return user;
//        }
//    }
}
