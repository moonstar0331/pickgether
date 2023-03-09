package com.capstone.pick.dto;

import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.VoteOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteOptionDto {
    private Long id;
    private VoteDto voteDto;
    private String content;
    private String imageLink;

    public static VoteOptionDto from(VoteOption entity) {
        return VoteOptionDto.builder()
                .id(entity.getId())
                .voteDto(VoteDto.from(entity.getVote()))
                .content(entity.getContent())
                .imageLink(entity.getImageLink())
                .build();
    }

    public VoteOption toEntity(Vote vote) {
        return VoteOption.builder()
                .vote(vote)
                .content(content)
                .imageLink(imageLink)
                .build();
    }
}
