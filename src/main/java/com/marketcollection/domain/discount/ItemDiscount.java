package com.marketcollection.domain.discount;

import com.marketcollection.domain.common.BaseTimeEntity;
import com.marketcollection.domain.discount.dto.DiscountRequestDto;
import com.marketcollection.domain.item.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class ItemDiscount extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Enumerated(EnumType.STRING)
    private DiscountStatus discountStatus;

    private int discountPrice;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;

    public static ItemDiscount createFlashDiscountItem(Item item, DiscountRequestDto dto) {
        return ItemDiscount.builder()
                .item(item)
                .discountType(dto.getDiscountType())
                .discountStatus(DiscountStatus.ON)
                .discountPrice((int) Math.round(item.getSalePrice() * dto.getDiscountRate() * 0.01))
                .startDate(dto.getStartDate())
                .finishDate(dto.getFinishDate())
                .build();
    }

    public void offSale() {
        this.discountStatus = DiscountStatus.OFF;
    }
}
