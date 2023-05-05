package com.capstone.pick.domain.constant;

import lombok.Getter;

@Getter
public enum RegionRestriction {
    Incheon("인천"), Seoul("서울"), Busan("부산"), All("전체");

    private final String displayValue;

    RegionRestriction(String displayValue) {
        this.displayValue = displayValue;
    }

}
