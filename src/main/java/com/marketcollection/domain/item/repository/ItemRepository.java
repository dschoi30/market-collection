package com.marketcollection.domain.item.repository;

import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.dto.ItemListDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    Boolean existsByIdLessThan(Long id);

    List<Item> findAllByOrderByIdDesc(Pageable pageable);

    List<Item> findByIdLessThanOrderByIdDesc(Long cursorItemId, Pageable pageable);
}
