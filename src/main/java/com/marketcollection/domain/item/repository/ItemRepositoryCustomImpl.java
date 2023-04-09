package com.marketcollection.domain.item.repository;

import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.QItem;
import com.marketcollection.domain.item.dto.ItemListDto;
import com.marketcollection.domain.item.dto.ItemSearchDto;
import com.marketcollection.domain.item.ItemSaleStatus;
import com.marketcollection.domain.item.dto.QItemListDto;
import com.marketcollection.domain.order.QOrderItem;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.marketcollection.domain.item.QItem.*;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory;
    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private static final int LIST_SIZE = 4;

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

    private BooleanExpression categoryIdEq(Long categoryId) {
        return ObjectUtils.isEmpty(categoryId) ? null : item.categoryId.eq(categoryId);
    }

    private OrderSpecifier orderSpecifier(ItemSearchDto itemSearchDto) {
        if (Objects.equals(itemSearchDto.getOrderBy(), "id"))
            return item.id.desc();
        if (Objects.equals(itemSearchDto.getOrderBy(), "itemName"))
            return item.itemName.asc();
        if (Objects.equals(itemSearchDto.getOrderBy(), "hit"))
            return item.hit.desc();
        if (Objects.equals(itemSearchDto.getOrderBy(), "salesCount"))
            return item.salesCount.desc();
        if (Objects.equals(itemSearchDto.getOrderBy(), "itemSaleStatus"))
            return item.itemSaleStatus.asc();
        if (Objects.equals(itemSearchDto.getOrderBy(), "createdBy"))
            return item.createdBy.asc();
        if (Objects.equals(itemSearchDto.getOrderBy(), "createdDate"))
            return item.createdDate.desc();
        if (Objects.equals(itemSearchDto.getOrderBy(), "salePriceAsc"))
            return item.salePrice.asc();
        if (Objects.equals(itemSearchDto.getOrderBy(), "salePriceDesc"))
            return item.salePrice.desc();
        return item.id.desc();
    }

    @Override
    public Page<ItemListDto> getItemListPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        List<Long> ids = queryFactory
                .select(item.id)
                .from(item)
                .where(
                        item.itemSaleStatus.eq(ItemSaleStatus.ON_SALE),
                        itemNameLike(itemSearchDto.getSearchQuery()),
                        categoryIdEq(itemSearchDto.getCategoryId())
                )
                .orderBy(orderSpecifier(itemSearchDto))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<ItemListDto> content = queryFactory
                .select(
                        new QItemListDto(
                                item.id,
                                item.itemName,
                                item.originalPrice,
                                item.salePrice,
                                item.repImageUrl,
                                item.reviewCount
                        ))
                .from(item)
                .where(item.id.in(ids))
                .orderBy(orderSpecifier(itemSearchDto))
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(item)
                .where(categoryIdEq(itemSearchDto.getCategoryId()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        List<Item> content = queryFactory
                .selectFrom(item)
                .where(
                        regDatesAfter(itemSearchDto.getSearchDateType()),
                        itemSaleStatusEq(itemSearchDto.getItemSaleStatus()),
                        searchBy(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .orderBy(orderSpecifier(itemSearchDto))
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

    @Override
    public List<ItemListDto> getRecentViewList(List<Long> itemIds) {
        return queryFactory
                .select(new QItemListDto(
                        item.id,
                        item.repImageUrl
                ))
                .from(item)
                .where(item.id.in(itemIds))
                .fetch();
    }

    @Cacheable("getWeeklyHotItemsCache")
    @Override
    public List<ItemListDto> getWeeklyHotItems() {
        QOrderItem orderItem = QOrderItem.orderItem;
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);

        List<Long> ids = queryFactory
                .select(orderItem.id)
                .from(orderItem)
                .where(orderItem.createdDate.after(oneWeekAgo))
                .fetch();

        return queryFactory
                .select(new QItemListDto(
                        item.id,
                        item.itemName,
                        item.originalPrice,
                        item.salePrice,
                        item.repImageUrl,
                        item.reviewCount
                ))
                .from(item)
                .join(orderItem).on(item.id.eq(orderItem.item.id))
                .where(orderItem.id.in(ids))
                .groupBy(item)
                .orderBy(orderItem.count.sum().desc())
                .limit(LIST_SIZE)
                .fetch();
    }

    @Cacheable("getMonthlyHighestDiscountRateItemsCache")
    @Override
    public List<ItemListDto> getMonthlyHighestDiscountRateItems() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        List<Long> ids = queryFactory
                .select(item.id)
                .from(item)
                .where(item.modifiedDate.after(oneMonthAgo))
                .fetch();

        return queryFactory
                .select(new QItemListDto(
                        item.id,
                        item.itemName,
                        item.originalPrice,
                        item.salePrice,
                        item.repImageUrl,
                        item.reviewCount
                ))
                .from(item)
                .where(item.id.in(ids))
                .orderBy(((item.originalPrice.subtract(item.salePrice)).multiply(1.0)).divide(item.originalPrice).desc())
                .limit(LIST_SIZE)
                .fetch();
    }
}
