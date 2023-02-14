package com.marketcollection.domain.order.repository;

import com.marketcollection.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
