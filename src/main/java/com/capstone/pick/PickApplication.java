package com.capstone.pick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class PickApplication {
    public static void main(String[] args) {
        SpringApplication.run(PickApplication.class, args);
    }
}
