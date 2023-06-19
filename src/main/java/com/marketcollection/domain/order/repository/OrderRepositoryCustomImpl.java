package com.marketcollection.domain.order.repository;

import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.OrderStatus;
import com.marketcollection.domain.order.QOrderItem;
import com.marketcollection.domain.order.dto.OrderHistoryDto2;
import com.marketcollection.domain.order.dto.OrderSearchDto;
import com.marketcollection.domain.order.dto.QOrderHistoryDto2;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.marketcollection.domain.order.QOrder.*;

public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private JPAQueryFactory queryFactory;
    public OrderRepositoryCustomImpl(EntityManager em) { this.queryFactory = new JPAQueryFactory(em); }

    private BooleanExpression regDatesAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("3m", searchDateType) || searchDateType == null) {
            dateTime = dateTime.minusMonths(3);
        } else if(StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        } else if(StringUtils.equals("1y", searchDateType)) {
            dateTime = dateTime.minusYears(1);
        } else if(StringUtils.equals("3y", searchDateType)) {
            dateTime = dateTime.minusYears(3);
        }
        return order.createdDate.after(dateTime);
    }

    @Override
    public List<Order> findOrders(Long memberId, OrderSearchDto orderSearchDto, Pageable pageable) {
        return queryFactory
                .selectFrom(order)
                .where(
                        order.member.id.eq(memberId),
                        order.orderStatus.in(
                                OrderStatus.READY, OrderStatus.IN_PROGRESS, OrderStatus.DONE,
                                OrderStatus.CANCELED, OrderStatus.PARTIAL_CANCELED),
                        regDatesAfter(orderSearchDto.getSearchDateType()))
                .orderBy(order.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Long countOrders(Long memberId, OrderSearchDto orderSearchDto) {
        return queryFactory
                .select(Wildcard.count)
                .from(order)
                .where(
                        order.member.id.eq(memberId),
                        regDatesAfter(orderSearchDto.getSearchDateType()))
                .fetchOne();
    }

    @Override
    public Page<OrderHistoryDto2> findOrderHistory(Long memberId, OrderSearchDto orderSearchDto, Pageable pageable) {
        QOrderItem orderItem = QOrderItem.orderItem;
        List<OrderHistoryDto2> content =  queryFactory
                .select(
                        new QOrderHistoryDto2(
                                order.orderNumber,
                                order.orderName,
                                order.paymentType,
                                order.totalPaymentAmount,
                                order.createdDate,
                                orderItem.repImageUrl,
                                order.orderStatus
                        ))
                .from(order)
                .join(orderItem).on(order.id.eq(orderItem.order.id))
                .where(
                        order.member.id.eq(memberId),
                        order.orderStatus.in(
                                OrderStatus.DONE, OrderStatus.CANCELED, OrderStatus.PARTIAL_CANCELED),
                        regDatesAfter(orderSearchDto.getSearchDateType()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(order.id.desc())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(order)
                .where(
                        order.member.id.eq(memberId),
                        order.orderStatus.in(
                                OrderStatus.DONE, OrderStatus.CANCELED, OrderStatus.PARTIAL_CANCELED),
                        regDatesAfter(orderSearchDto.getSearchDateType()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<Order> findAllOrders(OrderSearchDto orderSearchDto, Pageable pageable) {
        return queryFactory
                .selectFrom(order)
                .where(
                        regDatesAfter(orderSearchDto.getSearchDateType()))
                .orderBy(order.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Long countAllOrders(OrderSearchDto orderSearchDto) {
        return queryFactory
                .select(Wildcard.count)
                .from(order)
                .where(
                        regDatesAfter(orderSearchDto.getSearchDateType()))
                .fetchOne();
    }
}
