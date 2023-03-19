package com.capstone.pick.config;

import com.capstone.pick.domain.User;
import com.capstone.pick.repository.UserRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean
    private UserRepository userRepository;

    @BeforeTestMethod
    public void securitySetUp() {
        given(userRepository.findById("user")).willReturn(
                Optional.of(User.builder()
                        .userId("user")
                        .userPassword("password")
                        .email("test@email.com")
                        .nickname("user-test")
                        .memo("test memo")
                        .build()
                )
        );
    }
}
