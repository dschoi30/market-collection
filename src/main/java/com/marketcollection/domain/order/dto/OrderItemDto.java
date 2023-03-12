package com.marketcollection.domain.order.dto;

import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.order.OrderItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@ToString
@NoArgsConstructor
@Getter @Setter
public class OrderItemDto {

    private Long itemId;
    private String itemName;

    @Min(value = 1, message = "최소 1개 이상 주문되어야 합니다.")
    private int count;
    private int orderPrice;
    private int savingPoint;
    private String imageUrl;

    public OrderItemDto(Item item, int count, double savingRate) {
        this.itemId = item.getId();
        this.itemName = item.getItemName();
        this.count = count;
        this.orderPrice = item.getSalePrice() * count;
        this.savingPoint = (int) Math.round(item.getSalePrice() * count * savingRate);
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
