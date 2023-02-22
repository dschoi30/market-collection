package com.marketcollection.domain.item.repository;

import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.dto.ItemListDto;
import com.marketcollection.domain.item.dto.ItemSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepositoryCustom {

    Page<ItemListDto> getItemListPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    List<ItemListDto> findByIds(List<Long> itemIds);
}
