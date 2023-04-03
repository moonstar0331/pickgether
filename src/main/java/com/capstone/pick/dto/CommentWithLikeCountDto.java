package com.capstone.pick.dto;

import com.capstone.pick.domain.User;
import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.VoteComment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentWithLikeCountDto {

    private Long id;
    private Long voteId;
    private UserDto userDto;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    private Long likeCount;

    public static CommentWithLikeCountDto from(VoteComment entity) {
        return CommentWithLikeCountDto.builder()
                .id(entity.getId())
                .voteId(entity.getVote().getId())
                .userDto(UserDto.from(entity.getUser()))
                .content(entity.getContent())
                .createAt(entity.getCreateAt())
                .modifiedAt(entity.getModifiedAt())
                .likeCount(entity.getLikeCount())
                .build();
    }

    public VoteComment toEntity(User user, Vote vote) {
        return VoteComment.builder()
                .user(user)
                .vote(vote)
                .content(content)
                .createAt(createAt)
                .modifiedAt(modifiedAt)
                .likeCount(likeCount)
                .build();
    }
}
