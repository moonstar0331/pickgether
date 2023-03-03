package com.capstone.pick.controller.form;

import com.capstone.pick.domain.VoteOption;
import com.capstone.pick.domain.constant.Category;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class VoteForm {

    private String title;
    private Category category;
    private String content;
    private List<VoteOption> voteOptions;
}
