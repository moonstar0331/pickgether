package com.capstone.pick.controller.form;

import com.capstone.pick.domain.constant.*;
import com.capstone.pick.dto.HashtagDto;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.dto.VoteDto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteForm {
    private String title;
    private Category category;
    private String content;
    private List<VoteOptionFormDto> voteOptions;
    private Boolean isMultiPick;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expiredAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime modifiedAt;
    private DisplayRange displayRange; //공개범위
    private RegionRestriction regionRestriction; // 지역 제한
    private GenderRestriction genderRestriction; // 성별 제한
    private AgeRestriction ageRestriction; // 나이 제한

    public VoteDto toDto(UserDto userDto) {
        return VoteDto.builder()
                .userDto(userDto)
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

    @Override
    public String toString() {
        return "VoteForm{" +
                "title='" + title + '\'' +
                ", category=" + category +
                ", content='" + content + '\'' +
                ", voteOptions=" + voteOptions +
                ", isMultiPick=" + isMultiPick +
                ", expiredAt=" + expiredAt +
                ", createAt=" + createAt +
                ", modifiedAt=" + modifiedAt +
                ", displayRange=" + displayRange +
                ", regionRestriction=" + regionRestriction +
                ", genderRestriction=" + genderRestriction +
                ", ageRestriction=" + ageRestriction +
                '}';
    }

    public List<HashtagDto> getHashtagDtos() {
        Pattern pattern = Pattern.compile("#(.*?)\\s");
        Matcher matcher = pattern.matcher(content + " ");

        List<HashtagDto> hashtagDtos = new ArrayList<>();
        while (matcher.find()) {
            if (matcher.group(0).equals("# ")) {
                continue;
            }

            String[] split = matcher.group(1).split("#");
            for (String s : split) {
                hashtagDtos.add(HashtagDto.builder()
                        .content(s)
                        .build());
            }
        }

        return hashtagDtos;
    }
}
