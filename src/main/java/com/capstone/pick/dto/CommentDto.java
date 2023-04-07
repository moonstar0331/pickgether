package com.capstone.pick.dto;

import com.capstone.pick.domain.User;
import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.VoteComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private Long voteId;
    private UserDto userDto;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private Long likeCount;

    public static CommentDto from(VoteComment entity) {
        return CommentDto.builder()
                .id(entity.getId())
                .voteId(entity.getVote().getId())
                .userDto(UserDto.from(entity.getUser()))
                .content(entity.getContent())
                .createAt(entity.getCreateAt())
                .modifiedAt(entity.getModifiedAt())
                .build();
    }

    public VoteComment toEntity(User user, Vote vote) {
        return VoteComment.builder()
                .user(user)
                .vote(vote)
                .content(content)
                .createAt(createAt)
                .modifiedAt(modifiedAt)
                .build();
    }
}
