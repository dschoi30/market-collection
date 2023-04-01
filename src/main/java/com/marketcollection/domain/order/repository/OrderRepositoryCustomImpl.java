package com.marketcollection.domain.order.repository;

import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.dto.OrderSearchDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
