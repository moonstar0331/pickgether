package com.capstone.pick.domain.constant;

import lombok.Getter;

@Getter
public enum AgeRestriction {
    All("전체"), Ten("10대"), Twenty("20대"), Thirty("30대"), Forty("40대"), Fifty("50대") ;

    private final String displayValue;

    AgeRestriction(String displayValue) {
        this.displayValue = displayValue;
    }

}
