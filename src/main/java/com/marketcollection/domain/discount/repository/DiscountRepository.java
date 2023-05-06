package com.marketcollection.domain.discount.repository;

import com.marketcollection.domain.discount.ItemDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<ItemDiscount, Long> {
}
