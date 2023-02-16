package com.marketcollection.domain.cart.repository;

import com.marketcollection.domain.cart.CartItem;
import com.marketcollection.domain.cart.dto.CartItemDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndItemId(Long cartId, Long ItemId);

    @Query(
            "select new com.marketcollection.domain.cart.dto.CartItemDto(" +
                    "ci.id, im.itemImageUrl, i.id, i.itemName, i.originalPrice, i.salePrice, ci.count) " +
                    "from CartItem ci, ItemImage im " +
                    "join ci.item i " +
                    "where ci.cart.id = :cartId " +
                    "and im.item.id = ci.item.id " +
                    "and im.isRepImage = true " +
                    "order by ci.cart.modifiedDate desc"
    )
    List<CartItemDto> findCartItemList(@Param("cartId") Long cartId);

    @Query(
            "select ci " +
                    "from Item i " +
                    "join CartItem ci on (i.id = ci.item.id) " +
                    "join Cart c on (c.id = ci.cart.id) " +
                    "where c.member.id = :memberId " +
                    "and ci.item.id in :itemId"
    )
    CartItem findByMemberIdAndItemId(@Param("memberId") Long memberId, @Param("itemId") Long itemId);
}
