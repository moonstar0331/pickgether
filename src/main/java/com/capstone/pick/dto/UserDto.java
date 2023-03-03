package com.capstone.pick.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserDto {
    private String userId;
    private String userPassword;
    private String email;
    private String nickname;
    private String memo;
    private LocalDateTime birthday;
    private LocalDateTime createdAt;
}
