package com.capstone.pick.controller.form;

import com.capstone.pick.domain.constant.Category;
import com.capstone.pick.domain.constant.DisplayRange;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Builder
public class VoteForm {
    private String title;
    private Category category;
    private String content;
    private List<String> voteOption;
    private List<String> hashtag;
    private Boolean isMultiPick;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expiredAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime modifiedAt;
    private DisplayRange displayRange; //공개범위

    @Override
    public String toString() {
        return "VoteForm{" +
                "title='" + title + '\'' +
                ", category=" + category +
                ", content='" + content + '\'' +
                ", voteOption=" + voteOption +
                ", hashtag=" + hashtag +
                ", isMultiPick=" + isMultiPick +
                ", expiredAt=" + expiredAt +
                ", createAt=" + createAt +
                ", modifiedAt=" + modifiedAt +
                ", displayRange=" + displayRange +
                '}';
    }
}
