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

    public OrderItemDto(Item item, int count) {
        this.itemId = item.getId();
        this.itemName = item.getItemName();
        this.orderPrice = item.getSalePrice() * count;
        this.count = count;
        this.imageUrl = item.getRepImageUrl();
    }

    public OrderItemDto(OrderItem orderItem) {
        this.itemId = orderItem.getItem().getId();
        this.itemName = orderItem.getItem().getItemName();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imageUrl = orderItem.getRepImageUrl();
    }
}
