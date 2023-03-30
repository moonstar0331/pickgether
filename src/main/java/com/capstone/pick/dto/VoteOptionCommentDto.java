package com.capstone.pick.dto;

import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.constant.Category;
import com.capstone.pick.domain.constant.DisplayRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteOptionCommentDto {

    private Long id;
    private UserDto userDto;
    private String title;
    private String content;
    private Category category;
    private LocalDateTime expiredAt;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private boolean isMultiPick;
    private DisplayRange displayRange;
    private List<VoteOptionDto> voteOptionDtos;
    private CommentDto commentDto;

    public static VoteOptionCommentDto from(Vote entity) {
        return VoteOptionCommentDto.builder()
                .id(entity.getId())
                .userDto(UserDto.from(entity.getUser()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .category(entity.getCategory())
                .expiredAt(entity.getExpiredAt())
                .createAt(entity.getCreateAt())
                .modifiedAt(entity.getModifiedAt())
                .isMultiPick(entity.isMultiPick())
                .voteOptionDtos(entity.getVoteOptions().stream()
                        .map(VoteOptionDto::from).collect(Collectors.toList()))
                .commentDto(insertCommand(entity))
                .build();
    }

    private static CommentDto insertCommand(Vote vote) {
        if(vote.getVoteComments() == null || vote.getVoteComments().isEmpty()) {
            return null;
        }
        return CommentDto.from(vote.getVoteComments().get(0));
    }
}
