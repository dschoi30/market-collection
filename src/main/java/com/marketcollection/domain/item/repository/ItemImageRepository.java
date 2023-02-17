package com.marketcollection.domain.item.repository;

import com.marketcollection.domain.item.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long>, ItemImageRepositoryCustom {

    List<ItemImage> findByItemIdOrderByIdAsc(Long itemId);
}
