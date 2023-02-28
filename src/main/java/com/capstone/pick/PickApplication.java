package com.capstone.pick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class) // 시큐리티 로그인 창 때문에 임시 작성
public class PickApplication {
    public static void main(String[] args) {
        SpringApplication.run(PickApplication.class, args);
    }
}
