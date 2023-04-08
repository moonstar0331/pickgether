package com.capstone.pick.dto;

import com.capstone.pick.domain.Follow;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowDto {
    private Long id;
    private UserDto fromUser;
    private UserDto toUser;

    public static FollowDto from(Follow entity) {
        return FollowDto.builder()
                .id(entity.getId())
                .fromUser(UserDto.from(entity.getFromUser()))
                .toUser(UserDto.from(entity.getToUser()))
                .build();
    }

    public Follow toEntity() {
        return Follow.builder()
                .id(id)
                .fromUser(fromUser.toEntity())
                .toUser(toUser.toEntity())
                .build();
    }
}
