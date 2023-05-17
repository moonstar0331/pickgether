package com.capstone.pick.controller.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    private String userId;
    private String password;
    private String email;
    private String nickname;
    private String memo;
    private String birthday;

    private String gender;
    private final String provider = "HOMEPAGE"; // 가입방식

    private String job;
    private String address;
}
