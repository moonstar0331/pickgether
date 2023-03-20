package com.capstone.pick.dto;

import com.capstone.pick.domain.Follow;
import com.capstone.pick.domain.Hashtag;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowDto {
    private String fromUserId;
    private String toUserId;

    public static FollowDto from(Follow entity) {
        return FollowDto.builder()
                .fromUserId(entity.getFromUserId())
                .toUserId(entity.getToUserId())
                .build();
    }

    public Follow toEntity() {
        return Follow.builder()
                .fromUserId(fromUserId)
                .toUserId(toUserId)
                .build();
    }
}
