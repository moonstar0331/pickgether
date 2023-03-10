package com.capstone.pick.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VoteOptionFormListDto {
    private List<VoteOptionFormDto> voteOptions;
}
