package com.capstone.pick.controller.form;

import com.capstone.pick.dto.UserDto;
import com.capstone.pick.dto.VoteDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {

    private String userId;
    private String userPassword;
    private String email;
    private String nickname;
    private String memo;
    private String birthday;
    private String gender;
    private String age_range;


    public UserDto toDto() {
        return UserDto.builder()
                .userId(userId)
                .userPassword(userPassword)
                .email(email)
                .nickname(nickname)
                .memo(memo)
                .birthday(birthday)
                .gender(gender)
                .age_range(age_range)
                .build();

    }
}
