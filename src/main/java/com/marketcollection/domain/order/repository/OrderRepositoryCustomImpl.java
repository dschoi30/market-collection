package com.marketcollection.domain.order.repository;

import com.marketcollection.domain.delivery.QDelivery;
import com.marketcollection.domain.member.QMember;
import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.OrderStatus;
import com.marketcollection.domain.order.dto.*;
import com.marketcollection.domain.payment.QPayment;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.marketcollection.domain.delivery.QDelivery.*;
import static com.marketcollection.domain.member.QMember.*;
import static com.marketcollection.domain.order.QOrder.*;
import static com.marketcollection.domain.payment.QPayment.*;

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
    public List<OrderHistoryDto> findOrderHistory(Long memberId, String searchDateType, Pageable pageable) {
        return queryFactory
                .select(
                        new QOrderHistoryDto(
                                order.id,
                                order.orderNumber,
                                order.orderName,
                                order.paymentType,
                                order.totalPaymentAmount,
                                order.createdDate,
                                order.repImageUrl,
                                order.orderStatus
                        ))
                .from(order)
                .where(
                        order.member.id.eq(memberId),
                        order.orderStatus.in(
                                OrderStatus.READY, OrderStatus.IN_PROGRESS, OrderStatus.DONE,
                                OrderStatus.CANCELED, OrderStatus.PARTIAL_CANCELED),
                        regDatesAfter(searchDateType))
                .orderBy(order.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<OrderHistoryDto> findOrderHistoryLessThanId(Long memberId, String searchDateType, Long lastOrderId, Pageable pageable) {
        return queryFactory
                .select(
                        new QOrderHistoryDto(
                                order.id,
                                order.orderNumber,
                                order.orderName,
                                order.paymentType,
                                order.totalPaymentAmount,
                                order.createdDate,
                                order.repImageUrl,
                                order.orderStatus
                        ))
                .from(order)
                .where(
                        order.id.lt(lastOrderId),
                        order.member.id.eq(memberId),
                        order.orderStatus.in(
                                OrderStatus.READY, OrderStatus.IN_PROGRESS, OrderStatus.DONE,
                                OrderStatus.CANCELED, OrderStatus.PARTIAL_CANCELED),
                        regDatesAfter(searchDateType))
                .orderBy(order.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
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

    @Override
    public OrderDetailResponse findOrderDetail(Long orderId) {
        return queryFactory
                .select(new QOrderDetailResponse(
                        order.orderNumber,
                        order.usingPoint,
                        order.paymentType,
                        payment.paymentApprovedAt,
                        member.memberName,
                        member.memberName,
                        delivery.phoneNumber,
                        delivery.address.zipCode,
                        delivery.address.address,
                        delivery.address.detailAddress,
                        delivery.deliveryStatus
                        ))
                .from(order)
                .join(order.payment, payment)
                .join(order.delivery, delivery)
                .join(order.member, member)
                .where(order.id.eq(orderId))
                .fetchOne();
    }
}
