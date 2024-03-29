package com.marketcollection.domain.order;

import com.marketcollection.domain.common.BaseEntity;
import com.marketcollection.domain.item.Item;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(indexes = {@Index(name = "idx_order_item_created_date", columnList = "createdDate")})
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

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public OrderItem(Item item, Order order, String repImageUrl, int orderPrice) {
        this.item = item;
        this.order = order;
        this.repImageUrl = repImageUrl;
        this.orderPrice = orderPrice;
    }

    public static OrderItem createOrderItem(Item item, int count, float savingRate) {
        item.checkStock(count);
        item.deductStock(count);
        item.addSalesCount(count);

        return OrderItem.builder()
                .item(item)
                .repImageUrl(item.getRepImageUrl())
                .orderPrice(item.getSalePrice())
                .count(count)
                .savingPoint(Math.round(item.getSalePrice() * count * savingRate))
                .build();
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void cancel() {
        this.getItem().restoreStock(count);
    }
}
