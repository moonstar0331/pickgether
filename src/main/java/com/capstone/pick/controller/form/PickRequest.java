package com.capstone.pick.controller.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PickRequest {

    private Integer voteId;
    private Integer optionId;
}
