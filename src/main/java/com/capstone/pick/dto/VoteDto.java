package com.capstone.pick.dto;

import com.capstone.pick.domain.User;
import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.constant.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class VoteDto {
    private Long id;
    private UserDto userDto;
    private String title;
    private String content;
    private Category category;
    private LocalDateTime expiredAt;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private boolean isMultiPick;

    public Vote toEntity(User user) {
        return Vote.builder()
                .id(id)
                .user(user)
                .title(title)
                .content(content)
                .category(category)
                .expiredAt(expiredAt)
                .modifiedAt(modifiedAt)
                .isMultiPick(isMultiPick).build();
    }
}
