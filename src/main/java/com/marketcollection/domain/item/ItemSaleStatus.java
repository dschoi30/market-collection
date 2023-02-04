package com.marketcollection.domain.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ItemSaleStatus {
    ON_SALE("판매 중"), SOLD_OUT("재고 없음");

    private final String status;
}
