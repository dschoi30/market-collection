package com.marketcollection.domain.item.dto;

import lombok.Getter;

@Getter
public enum Sorter {
    ID("상품번호"), CATEGORY("카테고리"), ITEM_NAME("상품명"), HIT("조회수"), SALES_COUNT("판매수"), SALES_STATUS("판매상태"), CREATOR("등록자"), CREATED_DATE("등록일");

    private String sorter;

    Sorter(String sorter) {
        this.sorter = sorter;
    }
}
