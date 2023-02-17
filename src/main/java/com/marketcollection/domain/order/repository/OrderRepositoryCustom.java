package com.marketcollection.domain.order.repository;

import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.dto.OrderSearchDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findOrders(@Param("memberId") Long memberId, @Param("orderSearchDto") OrderSearchDto orderSearchDto);

    Long countOrders(@Param("memberId") Long memberId, @Param("orderSearchDto") OrderSearchDto orderSearchDto);
}
