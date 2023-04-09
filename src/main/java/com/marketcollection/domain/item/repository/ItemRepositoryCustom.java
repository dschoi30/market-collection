package com.marketcollection.domain.item.repository;

import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.dto.ItemListDto;
import com.marketcollection.domain.item.dto.ItemSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {
    Page<ItemListDto> getItemListPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    List<ItemListDto> getRecentViewList(List<Long> itemIds);

    List<ItemListDto> getWeeklyHotItems();

    List<ItemListDto> getMonthlyHighestDiscountRateItems();
}
