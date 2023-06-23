package com.marketcollection.domain.order.repository;

import com.marketcollection.domain.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByIdIn(List<Long> orderItemIds);

    List<OrderItem> findByOrderId(Long orderId);
}
