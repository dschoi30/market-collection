package com.marketcollection.domain.item.repository;

import com.marketcollection.domain.item.dto.ItemListDto;
import com.marketcollection.domain.item.dto.ItemSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<ItemListDto> getItemListPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
