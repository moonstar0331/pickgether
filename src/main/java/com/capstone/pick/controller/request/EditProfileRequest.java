package com.capstone.pick.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditProfileRequest {
    private String nickname;
    private String birthday;
    private String gender;
    private String job;
    private String memo;
    private String address;
}
