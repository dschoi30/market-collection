package com.marketcollection.domain.order.dto;

import com.marketcollection.domain.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearchDto {
    private String searchDateType; // 3개월, 6개월, 1년, 3년
    private String searchBy; // 주문번호, 등록자
    private OrderStatus orderStatus; // 주문 상태
    private String orderBy; // 상품번호, 카테고리, 상품명, 조회수, 판매량, 판매상태, 등록자, 등록일

    public OrderSearchDto(String searchDateType, String searchBy, OrderStatus orderStatus, String orderBy) {
        this.searchDateType = searchDateType;
        this.searchBy = searchBy;
        this.orderStatus = orderStatus;
        this.orderBy = orderBy;
    }
}
