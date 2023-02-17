package com.marketcollection.domain.order.repository;

import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.dto.OrderSearchDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
/*    @Query(
            "select o " +
                    "from Order o " +
                    "where o.member.id = :memberId " +
                    "order by o.createdDate desc"
    )
    List<Order> findOrders(@Param("memberId") Long memberId, OrderSearchDto orderSearchDto, Pageable pageable);

    @Query(
            "select count(o) " +
                    "from Order o " +
                    "where o.member.id = :memberId"
    )
    Long countOrders(@Param("memberId") Long memberId);*/
}
