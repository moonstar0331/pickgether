package com.capstone.pick.domain.constant;

public enum DisplayRange {
    PUBLIC("전체 공개"), FRIEND("친구 공개"), LIST("친한 친구 공개");

    private final String displayValue;

    private DisplayRange(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
