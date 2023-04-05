package com.marketcollection.domain.item.repository;

import com.marketcollection.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    Boolean existsByIdLessThan(Long id);
}
