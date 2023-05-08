package com.marketcollection.domain.discount.dto;

import com.marketcollection.domain.discount.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DiscountRequestDto {

    @NonNull private List<Long> itemIds = new ArrayList<>();
    @NonNull private DiscountType discountType;
    @NonNull private int discountRate; // 0 ~ 100
    private LocalDateTime startDate;
    private LocalDateTime finishDate;

    public DiscountRequestDto(List<Long> itemIds) {
        this.itemIds = itemIds;
    }
}
