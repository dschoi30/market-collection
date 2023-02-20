package com.marketcollection.domain.item.repository;

import com.marketcollection.domain.item.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

    List<ItemImage> findByItemIdOrderByIdAsc(Long itemId);

    @Query("select im.itemImageUrl from ItemImage im where im.item.id = :id and im.isRepImage = :true")
    String findByItemIdAndRepImageIsTrue(@Param("id") Long id, @Param("true") boolean isRepImage);
}
