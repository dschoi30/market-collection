package com.marketcollection.domain.order.dto;

import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
public class OrderResponseDto {

    public String orderNumber;
    public String orderName;
    public int orderPrice;
    public String memberName;
    public String memberEmail;
}
