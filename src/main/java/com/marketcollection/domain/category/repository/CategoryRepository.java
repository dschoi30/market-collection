package com.marketcollection.domain.category.repository;

import com.marketcollection.domain.category.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<ItemCategory, Long> {
}
