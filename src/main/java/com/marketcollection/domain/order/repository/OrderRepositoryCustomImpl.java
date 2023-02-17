package com.marketcollection.domain.order.repository;

import com.marketcollection.domain.order.Order;
import com.marketcollection.domain.order.QOrder;
import com.marketcollection.domain.order.dto.OrderSearchDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

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
        return QOrder.order.createdDate.after(dateTime);
    }

    @Override
    public List<Order> findOrders(Long memberId, OrderSearchDto orderSearchDto) {
        QOrder order = QOrder.order;
        return queryFactory
                .selectFrom(order)
                .where(
                        order.member.id.eq(memberId),
                        regDatesAfter(orderSearchDto.getSearchDateType()))
                .orderBy(order.id.desc())
                .fetch();
    }

    @Override
    public Long countOrders(Long memberId, OrderSearchDto orderSearchDto) {
        QOrder order = QOrder.order;
        return queryFactory
                .select(Wildcard.count)
                .from(order)
                .where(
                        order.member.id.eq(memberId),
                        regDatesAfter(orderSearchDto.getSearchDateType()))
                .orderBy(order.id.desc())
                .fetchOne();
    }
}
