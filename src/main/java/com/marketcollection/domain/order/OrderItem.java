package com.marketcollection.domain.order;

import com.marketcollection.domain.common.BaseEntity;
import com.marketcollection.domain.item.Item;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class OrderItem extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private String repImageUrl;
    private int orderPrice;
    private int count;
    private int savingPoint;

    public OrderItem(Item item, Order order, String repImageUrl, int orderPrice) {
        this.item = item;
        this.order = order;
        this.repImageUrl = repImageUrl;
        this.orderPrice = orderPrice;
    }

    public static OrderItem createOrderItem(Item item, int count, double savingRate) {
        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .repImageUrl(item.getRepImageUrl())
                .orderPrice(item.getSalePrice())
                .count(count)
                .savingPoint((int) Math.round(item.getSalePrice() * savingRate))
                .build();
        item.deductStock(count);
        return orderItem;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void cancel() {
        this.getItem().restoreStock(count);
    }
}
