package com.marketcollection.domain.discount;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DiscountStatus {
    ON("진행 중"), OFF("종료");

    private final String status;
}
