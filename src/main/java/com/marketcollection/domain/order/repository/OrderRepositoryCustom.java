package com.marketcollection.domain.order.repository;

import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.dto.OrderDetailResponse;
import com.marketcollection.domain.order.dto.OrderHistoryDto;
import com.marketcollection.domain.order.dto.OrderSearchDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findOrders(@Param("memberId") Long memberId, @Param("orderSearchDto") OrderSearchDto orderSearchDto, Pageable pageable);

    Long countOrders(@Param("memberId") Long memberId, @Param("orderSearchDto") OrderSearchDto orderSearchDto);

    List<OrderHistoryDto> findOrderHistory(@Param("memberId") Long memberId, @Param("searchDateType") String searchDateType, Pageable pageable);

    List<OrderHistoryDto> findOrderHistoryLessThanId(@Param("memberId") Long memberId, @Param("searchDateType") String searchDateType, Long lastOrderId, Pageable pageable);

    List<Order> findAllOrders(@Param("orderSearchDto") OrderSearchDto orderSearchDto, Pageable pageable);

    Long countAllOrders(@Param("orderSearchDto") OrderSearchDto orderSearchDto);

    OrderDetailResponse findOrderDetail(@Param("orderId") Long orderId);
}
