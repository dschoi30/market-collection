package com.marketcollection.domain.discount.repository;

import com.marketcollection.domain.discount.ItemDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiscountRepository extends JpaRepository<ItemDiscount, Long>, DiscountRepositoryCustom {
    @Query("select i from ItemDiscount i where i.discountStatus = 'ON'")
    List<ItemDiscount> findOnGoingItemDiscount();
}
