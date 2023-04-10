package com.capstone.pick.dto;

import com.capstone.pick.domain.Bookmark;
import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDto {
    private Long id;
    private UserDto userDto;
    private VoteDto voteDto;
    private LocalDateTime createAt;

    public static BookmarkDto from(Bookmark entity) {
        return BookmarkDto.builder()
                .id(entity.getId())
                .userDto(UserDto.from(entity.getUser()))
                .voteDto(VoteDto.from(entity.getVote()))
                .createAt(entity.getCreateAt())
                .build();
    }

    public Bookmark toEntity(User user, Vote vote) {
        return Bookmark.builder()
                .user(userDto.toEntity())
                .vote(voteDto.toEntity(userDto.toEntity()))
                .createAt(createAt)
                .build();
    }
}
