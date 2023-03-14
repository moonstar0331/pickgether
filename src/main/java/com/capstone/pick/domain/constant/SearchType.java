package com.capstone.pick.domain.constant;

import lombok.Getter;

public enum SearchType {

    USER("유저"),
    TITLE("제목"),
    CONTENT("본문"),
    NICKNAME("닉네임"),
    HASHTAG("해시태그");

    @Getter
    private final String description;

    SearchType(String description) {
        this.description = description;
    }
}
