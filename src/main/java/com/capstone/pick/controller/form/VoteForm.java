package com.capstone.pick.controller.form;

import com.capstone.pick.domain.VoteOption;
import com.capstone.pick.domain.constant.Category;
import com.capstone.pick.domain.constant.DisplayRange;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class VoteForm {

    private String title;
    private Category category;
    private String content;
    private List<VoteOption> voteOptions;
    private boolean isMultiPick;
    private LocalDateTime expiredAt;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private DisplayRange displayRange;//공개범위
}
