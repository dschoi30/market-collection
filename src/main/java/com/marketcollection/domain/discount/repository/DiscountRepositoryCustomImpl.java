package com.marketcollection.domain.discount.repository;

import com.marketcollection.domain.discount.DiscountStatus;
import com.marketcollection.domain.discount.dto.DailySaleItemListDto;
import com.marketcollection.domain.discount.dto.DiscountResponseDto;
import com.marketcollection.domain.discount.dto.QDailySaleItemListDto;
import com.marketcollection.domain.discount.dto.QDiscountResponseDto;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static com.marketcollection.domain.discount.QItemDiscount.*;
import static com.marketcollection.domain.item.QItem.item;

public class DiscountRepositoryCustomImpl implements DiscountRepositoryCustom {

    private JPAQueryFactory queryFactory;
    public DiscountRepositoryCustomImpl(EntityManager em) { this.queryFactory = new JPAQueryFactory(em); }

    private static final int LIST_SIZE = 3;

    @Override
    public Page<DiscountResponseDto> getItemDiscountList(Pageable pageable) {
        List<DiscountResponseDto> content = queryFactory
                .select(
                        new QDiscountResponseDto(
                                itemDiscount.id,
                                itemDiscount.discountStatus,
                                itemDiscount.discountType,
                                itemDiscount.item.id,
                                itemDiscount.item.repImageUrl,
                                itemDiscount.item.itemName,
                                itemDiscount.item.salePrice,
                                itemDiscount.discountPrice,
                                itemDiscount.startDate,
                                itemDiscount.finishDate
                        )
                )
                .from(itemDiscount)
                .orderBy(itemDiscount.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemDiscount)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<DailySaleItemListDto> getDailySaleItems() {
        return queryFactory
                .select(new QDailySaleItemListDto(
                        itemDiscount.id,
                        item.id,
                        item.itemName,
                        item.originalPrice,
                        item.salePrice,
                        item.discountPrice,
                        item.repImageUrl,
                        item.reviewCount,
                        itemDiscount.finishDate
                ))
                .from(item)
                .leftJoin(itemDiscount).on(item.id.eq(itemDiscount.item.id))
                .where(
                        itemDiscount.discountStatus.eq(DiscountStatus.ON),
                        itemDiscount.startDate.loe(LocalDateTime.now()),
                        itemDiscount.finishDate.goe(LocalDateTime.now())
                )
                .orderBy(itemDiscount.id.desc())
                .limit(LIST_SIZE)
                .fetch();
    }
}
