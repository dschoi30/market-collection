package com.marketcollection.domain.item.repository;

import com.marketcollection.domain.item.ItemImage;
import com.marketcollection.domain.item.QItemImage;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import static com.marketcollection.domain.item.QItemImage.*;

public class ItemImageRepositoryCustomImpl implements ItemImageRepositoryCustom {
    private JPAQueryFactory queryFactory;

    public ItemImageRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public ItemImage findByItemIdAndRepImageIsTrue(Long itemId, boolean isRepImage) {
        return queryFactory
                .selectFrom(itemImage)
                .where(
                        itemImage.item.id.eq(itemId),
                        itemImage.isRepImage.isTrue()
                )
                .fetchOne();
    }
}
