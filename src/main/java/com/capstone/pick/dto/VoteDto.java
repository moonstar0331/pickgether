package com.capstone.pick.dto;

import com.capstone.pick.domain.User;
import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.constant.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private DisplayRange displayRange; // 공개 범위
    private RegionRestriction regionRestriction; // 지역 제한
    private GenderRestriction genderRestriction; // 성별 제한
    private AgeRestriction ageRestriction; // 나이 제한

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
                .regionRestriction(entity.getRegionRestriction())
                .genderRestriction(entity.getGenderRestriction())
                .ageRestriction(entity.getAgeRestriction())
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
                .regionRestriction(regionRestriction)
                .genderRestriction(genderRestriction)
                .ageRestriction(ageRestriction)
                .build();
    }
}
