package com.capstone.pick.domain.constant;

import lombok.Getter;

@Getter
public enum GenderRestriction {
    Male("남성"), Female("여성"), All("전체");

    private final String displayValue;

    GenderRestriction(String displayValue) {
        this.displayValue = displayValue;
    }

}
