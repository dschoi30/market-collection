package com.marketcollection.domain.order.dto;

import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.order.OrderItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class OrderItemDto {

    private Long itemId;
    private String itemName;
    private int count;
    private int orderPrice;
    private String imageUrl;

    public OrderItemDto(Long itemId, String itemName, int salePrice, int count) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.orderPrice = salePrice;
        this.count = count;
    }
}
