package com.marketcollection.domain.order.repository;

import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.dto.OrderSearchDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    Optional<Order> findByOrderNumber(String orderNumber);
}
