package com.capstone.pick.dto;

import com.capstone.pick.domain.Pick;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickCachingDto {

    private Long id;
    private Long voteId;
    private Long voteOptionId;
    private String userId;

    public static PickCachingDto from(Pick entity, Long voteId) {
        return PickCachingDto.builder()
                .id(entity.getId())
                .voteId(voteId)
                .voteOptionId(entity.getVoteOption().getId())
                .userId(entity.getUser().getUserId())
                .build();
    }
}
