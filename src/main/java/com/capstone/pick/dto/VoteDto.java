package com.capstone.pick.dto;

import com.capstone.pick.controller.form.VoteOptionFormDto;
import com.capstone.pick.domain.User;
import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.constant.Category;
import com.capstone.pick.domain.constant.DisplayRange;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteDto {
    private Long id;
    private UserDto userDto;
    private String title;
    private String content;
    private List<VoteOptionFormDto> voteOptions;
    private Category category;
    private LocalDateTime expiredAt;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private boolean isMultiPick;
    private DisplayRange displayRange;

    public static VoteDto from(Vote entity) {
        return VoteDto.builder()
                .id(entity.getId())
                .userDto(UserDto.from(entity.getUser()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .category(entity.getCategory())
                .expiredAt(entity.getExpiredAt())
                .createAt(entity.getCreateAt())
                .modifiedAt(entity.getModifiedAt())
                .isMultiPick(entity.isMultiPick())
                .build();
    }

    public Vote toEntity(User user) {
        return Vote.builder()
                .user(user)
                .title(title)
                .content(content)
                .category(category)
                .expiredAt(expiredAt)
                .isMultiPick(isMultiPick)
                .displayRange(displayRange)
                .build();
    }
}
