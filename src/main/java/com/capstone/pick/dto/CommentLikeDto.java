package com.capstone.pick.dto;

import com.capstone.pick.domain.CommentLike;
import com.capstone.pick.domain.User;
import com.capstone.pick.domain.VoteComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("LIKE")
public class CommentLikeDto {

    @Id
    private Long id;
    @Indexed
    private Long voteCommentId;
    private UserDto userDto;

    public static CommentLikeDto from(CommentLike entity) {
        return CommentLikeDto.builder()
                .id(entity.getId())
                .voteCommentId(entity.getVoteComment().getId())
                .userDto(UserDto.from(entity.getUser()))
                .build();
    }

    public CommentLike toEntity(User user, VoteComment voteComment) {
        return CommentLike.builder()
                .user(user)
                .voteComment(voteComment)
                .build();
    }
}
