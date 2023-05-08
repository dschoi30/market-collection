package com.marketcollection.domain.discount.repository;

import com.marketcollection.domain.discount.ItemDiscount;
import com.marketcollection.domain.discount.QItemDiscount;
import com.marketcollection.domain.discount.dto.DiscountResponseDto;
import com.marketcollection.domain.discount.dto.QDiscountResponseDto;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.List;

import static com.marketcollection.domain.discount.QItemDiscount.*;

public class DiscountRepositoryCustomImpl implements DiscountRepositoryCustom {

    private JPAQueryFactory queryFactory;
    public DiscountRepositoryCustomImpl(EntityManager em) { this.queryFactory = new JPAQueryFactory(em); }

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
                                itemDiscount.discountPrice
                        )
                )
                .from(itemDiscount)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemDiscount)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
