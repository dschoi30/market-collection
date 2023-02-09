package com.marketcollection.domain.item.repository;

import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.dto.ItemListDto;
import com.marketcollection.domain.item.dto.ItemSearchDto;
import com.marketcollection.api.item.dto.QItemListDto;
import com.marketcollection.domain.item.ItemSaleStatus;
import com.marketcollection.domain.item.QItem;
import com.marketcollection.domain.item.dto.Sorter;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory;
    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    QItem item = QItem.item;

    private BooleanExpression regDatesAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if(StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }
        return item.createdDate.after(dateTime);
    }

    private BooleanExpression itemSaleStatusEq(ItemSaleStatus itemSaleStatus) {
        return itemSaleStatus == null ? null : item.itemSaleStatus.eq(itemSaleStatus);
    }

    private BooleanExpression searchBy(String searchBy, String searchQuery) {
        if(StringUtils.equals("itemName", searchBy)) {
            return item.itemName.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)) {
            return item.createdBy.like("%" + searchQuery + "%");
        }
        return null;
    }

    private BooleanExpression itemNameLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : item.itemName.like("%" + searchQuery + "%");
    }

    @Override
    public Page<ItemListDto> getItemListPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        List<ItemListDto> content = queryFactory
                .select(
                        new QItemListDto(
                                item.id,
                                item.itemName,
                                item.originalPrice,
                                item.salePrice,
                                item.thumbnailImageFile
                        ))
                .from(item)
                .where(item.itemSaleStatus.eq(ItemSaleStatus.ON_SALE))
                .where(itemNameLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(item)
                .where(itemNameLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
//
//    @Override
//    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
//        List<Item> content = queryFactory
//                .selectFrom(item)
//                .where(
//                        regDatesAfter(itemSearchDto.getSearchDateType()),
//                        itemSaleStatusEq(itemSearchDto.getItemSaleStatus()),
//                        searchBy(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
//                .orderBy(item.id.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        long total = queryFactory
//                .select(Wildcard.count)
//                .from(item)
//                .where(
//                        regDatesAfter(itemSearchDto.getSearchDateType()),
//                        itemSaleStatusEq(itemSearchDto.getItemSaleStatus()),
//                        searchBy(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
//                .fetchOne();
//
//        return new PageImpl<>(content, pageable, total);
//    }

//    @Override
//    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
//        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);
//
//        List<Item> content = queryFactory
//                .selectFrom(item)
//                .where(
//                        regDatesAfter(itemSearchDto.getSearchDateType()),
//                        itemSaleStatusEq(itemSearchDto.getItemSaleStatus()),
//                        searchBy(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
//                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//                long total = queryFactory
//                .select(Wildcard.count)
//                .from(item)
//                .where(
//                        regDatesAfter(itemSearchDto.getSearchDateType()),
//                        itemSaleStatusEq(itemSearchDto.getItemSaleStatus()),
//                        searchBy(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
//                .fetchOne();
//
//        return new PageImpl<>(content, pageable, total);
//    }
//        private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
//            List<OrderSpecifier> ORDERS = new ArrayList<>();
//
//            if (!isEmpty(pageable.getSort())) {
//                for (Sort.Order order : pageable.getSort()) {
//                    Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
//
//                    switch (order.getProperty()) {
//                        case "salesCount":
//                            OrderSpecifier<?> salesCount = QueryDslUtil
//                                    .getSortedColumn(direction, QItem.item, "salesCount");
//                            ORDERS.add(salesCount);
//                            break;
//                        default:
//                            break;
//                    }
//                }
//            }
//
//            return ORDERS;
//        }
//
//    public static class QueryDslUtil {
//
//        public static OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
//            Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);
//
//            return new OrderSpecifier(order, fieldPath);
//        }
//    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        List<Item> content = queryFactory
                .selectFrom(item)
                .where(
                        regDatesAfter(itemSearchDto.getSearchDateType()),
                        itemSaleStatusEq(itemSearchDto.getItemSaleStatus()),
                        searchBy(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .orderBy(sorter(itemSearchDto.getSorter()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(item)
                .where(
                        regDatesAfter(itemSearchDto.getSearchDateType()),
                        itemSaleStatusEq(itemSearchDto.getItemSaleStatus()),
                        searchBy(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private OrderSpecifier sorter(Sorter sorter) {
        if (sorter == null)
            return item.id.desc();

        if (sorter == Sorter.CATEGORY)
            return item.categoryId.desc();

        if (sorter == Sorter.ITEM_NAME)
            return item.itemName.desc();

        if (sorter == Sorter.HIT)
            return item.hit.desc();

        if (sorter == Sorter.SALES_COUNT)
            return item.salesCount.desc();

        return item.id.desc();
    }
}
