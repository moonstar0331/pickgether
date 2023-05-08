package com.capstone.pick.dto;

import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.constant.*;
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
    private DisplayRange displayRange; // 공개 범위
    private RegionRestriction regionRestriction; // 지역제한
    private GenderRestriction genderRestriction; // 성별제한
    private AgeRestriction ageRestriction; // 나이 제한
    private List<VoteOptionDto> voteOptionDtos;
    private List<CommentDto> commentDtos;
    private Long pickCount;

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
                .commentDtos(entity.getVoteComments().stream()
                        .map(CommentDto::from).collect(Collectors.toList()))
                .pickCount(entity.getPickCount())
                .regionRestriction(entity.getRegionRestriction())
                .genderRestriction(entity.getGenderRestriction())
                .displayRange(entity.getDisplayRange())
                .ageRestriction(entity.getAgeRestriction())
                .build();
    }
}
