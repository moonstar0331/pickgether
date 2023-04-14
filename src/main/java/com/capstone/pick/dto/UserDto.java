package com.capstone.pick.dto;

import com.capstone.pick.domain.Follow;
import com.capstone.pick.domain.User;
import lombok.*;

import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private List<FollowDto> following = new ArrayList<>();

    private List<FollowDto> followers = new ArrayList<>();

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
                .following(entity.getFollowing().stream().map(FollowDto::from).collect(Collectors.toList()))
                .followers(entity.getFollowers().stream().map(FollowDto::from).collect(Collectors.toList()))
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
                .following(following.stream().map(FollowDto::toEntity).collect(Collectors.toList()))
                .followers(followers.stream().map(FollowDto::toEntity).collect(Collectors.toList()))
                .build();
    }
}
