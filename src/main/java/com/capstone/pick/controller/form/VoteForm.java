package com.capstone.pick.controller.form;

import com.capstone.pick.domain.constant.Category;
import com.capstone.pick.domain.constant.DisplayRange;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.dto.VoteDto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;


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
//    private List<String> hashtag;
    private Boolean isMultiPick;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expiredAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime modifiedAt;
    private DisplayRange displayRange; //공개범위

    public VoteDto toDto(UserDto userDto) {
        return VoteDto.builder()
                .userDto(userDto)
                .title(title)
                .content(content)
                .category(category)
                .expiredAt(expiredAt)
                .isMultiPick(isMultiPick)
                .displayRange(displayRange)
                .build();
    }

    @Override
    public String toString() {
        return "VoteForm{" +
                "title='" + title + '\'' +
                ", category=" + category +
                ", content='" + content + '\'' +
                ", voteOptions=" + voteOptions +
//                ", hashtag=" + hashtag +
                ", isMultiPick=" + isMultiPick +
                ", expiredAt=" + expiredAt +
                ", createAt=" + createAt +
                ", modifiedAt=" + modifiedAt +
                ", displayRange=" + displayRange +
                '}';
    }
}
