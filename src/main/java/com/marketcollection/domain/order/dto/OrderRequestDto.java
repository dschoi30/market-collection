package com.marketcollection.domain.order.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class OrderRequestDto {

    private List<OrderItemRequestDto> orderItemRequestDtos;
}
