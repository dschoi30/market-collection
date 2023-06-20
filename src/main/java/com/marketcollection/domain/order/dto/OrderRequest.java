package com.marketcollection.domain.order.dto;

import lombok.*;

import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class OrderRequest {

    private List<OrderItemRequest> orderItemRequests;
}
