package com.capstone.pick.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentPostDto {

    private CommentDto commentDto;

    private Long likeCount;

    private Long likeId;
}
