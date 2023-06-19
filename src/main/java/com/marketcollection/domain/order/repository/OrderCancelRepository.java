package com.marketcollection.domain.order.repository;

import com.marketcollection.domain.order.OrderCancel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderCancelRepository extends JpaRepository<OrderCancel, Long> {
}
