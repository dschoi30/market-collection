package com.marketcollection.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class CartRequestDto {
    private Long itemId;

    @Min(value = 1, message = "최소 1개 이상 담아야 합니다.")
    private int count;
}
