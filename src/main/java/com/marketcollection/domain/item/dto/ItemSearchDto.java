package com.marketcollection.domain.item.dto;

import com.marketcollection.domain.item.ItemSaleStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchDto {

    private String searchDateType; // 1일, 1주일, 1개월, 6개월
    private ItemSaleStatus itemSaleStatus; // 판매 중, 품절
    private String searchBy; // 상품명, 등록자
    private String searchQuery = ""; // 검색어
    private String orderBy; // 상품번호, 카테고리, 상품명, 조회수, 판매량, 판매상태, 등록자, 등록일
}
