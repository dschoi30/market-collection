package com.marketcollection.domain.point;

import com.marketcollection.domain.common.BaseEntity;
import com.marketcollection.domain.member.Member;
import com.marketcollection.domain.order.OrderItem;
import com.marketcollection.domain.order.dto.OrderDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Point extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    private int point;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private LocalDateTime expireDate;

    public static Point createOrderPoint(Member member, OrderItem orderItem) {
        return Point.builder()
                .member(member)
                .orderItem(orderItem)
                .point(orderItem.getSavingPoint())
                .eventType(EventType.ORDER)
                .expireDate(LocalDateTime.now().plusYears(1))
                .build();
    }

    public static Point createUsingPoint(Member member, OrderDto orderDto) {
        return Point.builder()
                .member(member)
                .point(orderDto.getUsingPoint())
                .eventType(EventType.USED)
                .build();
    }
}
