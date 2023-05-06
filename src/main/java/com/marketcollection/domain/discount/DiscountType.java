package com.marketcollection.domain.discount;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DiscountType {
    DAILY_SALE("일일 특가");

    private final String title;
}
