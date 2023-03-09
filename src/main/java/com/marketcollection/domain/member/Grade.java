package com.marketcollection.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Grade {
    NORMAL("일반"), FRIENDS("프렌즈"), WHITE("화이트"), LAVENDER("라벤더"), PURPLE("퍼플"), THE_PURPLE("더퍼플");

    private final String title;
}
