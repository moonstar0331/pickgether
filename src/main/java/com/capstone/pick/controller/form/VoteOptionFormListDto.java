package com.capstone.pick.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VoteOptionFormListDto {
    private List<VoteOptionFormDto> voteOptions;

    public void printList() {
        for (VoteOptionFormDto option : voteOptions) {
            System.out.println("[" + voteOptions.indexOf(option) + "] content : " +
                    option.getContent() + " | type : " + option.getClass().getName());
        }
    }
}
