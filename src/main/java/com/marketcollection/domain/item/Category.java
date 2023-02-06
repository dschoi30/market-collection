package com.marketcollection.domain.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor
@Getter
public enum Category {
    VEGETABLE("채소"),
    FRUIT_RICE("과일, 쌀"),
    SEAFOOD("수산, 해산물"),
    MEAT_EGG("정육, 계란"),
    SOUP_SIDEDISH_MAINDISH("국, 반찬, 메인요리"),
    SALAD("샐러드"),
    NOODLE_OIL("면, 양념, 오일"),
    WATER_MILK_JUICE_COFFEE("물, 우유, 주스, 커피"),
    DESSERT_SNACK("디저트, 과자"),
    ECO_FRIENDLY_FOOD("친환경");

    private final String title;
}
