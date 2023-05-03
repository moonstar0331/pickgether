package com.capstone.pick.dto;

import com.capstone.pick.domain.User;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String userId;
    private String userPassword;
    private String email;
    private String nickname;
    private String memo;
    private String birthday;
    private LocalDateTime createdAt;

    private String gender;
    private String age_range;
    private String provider; // 가입방식

    private String job;
    private String address;

    public static UserDto from(User entity) {
        return UserDto.builder()
                .userId(entity.getUserId())
                .userPassword(entity.getUserPassword())
                .email(entity.getEmail())
                .nickname(entity.getNickname())
                .memo(entity.getMemo())
                .birthday(entity.getBirthday())
                .createdAt(entity.getCreatedAt())
                .gender(entity.getGender())
                .age_range(entity.getAge_range())
                .provider(entity.getProvider())
                .address(entity.getAddress())
                .job(entity.getJob())
                .build();
    }

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .userPassword(userPassword)
                .email(email)
                .nickname(nickname)
                .memo(memo)
                .birthday(birthday)
                .gender(gender)
                .age_range(age_range)
                .provider(provider)
                .address(address)
                .job(job)
                .build();
    }

    public void updateInfo(String nickname, String birthday, String gender, String job, String memo) {
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
        this.job = job;
        this.memo = memo;
    }
}
