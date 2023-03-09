package com.capstone.pick.domain.constant;

public enum Category {
    FREE("자유"), WORRY("고민"), SURVEY("설문"), ENTERPRISE("기업");

    private final String displayValue;

    Category(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
