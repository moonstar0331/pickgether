package com.capstone.pick.controller.form;

import com.capstone.pick.dto.VoteDto;
import com.capstone.pick.dto.VoteOptionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteOptionFormDto {
    private String content;
    private String imageLink;

    public VoteOptionDto toDto(VoteDto voteDto) {
        return VoteOptionDto.builder()
                .voteDto(voteDto)
                .content(content)
                .imageLink(imageLink)
                .build();
    }
}
