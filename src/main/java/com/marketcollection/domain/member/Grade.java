package com.marketcollection.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Grade {
    NORMAL("일반", 0.005f),
    FRIENDS("프렌즈", 0.01f),
    WHITE("화이트", 0.03f),
    LAVENDER("라벤더", 0.05f),
    PURPLE("퍼플", 0.07f),
    THE_PURPLE("더퍼플", 0.08f);

    private final String title;
    private final float pointSavingRate;
}
