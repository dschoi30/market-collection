package com.marketcollection.domain.discount.repository;

import com.marketcollection.domain.discount.dto.DailySaleItemListDto;
import com.marketcollection.domain.discount.dto.DiscountResponseDto;
import com.marketcollection.domain.item.dto.ItemListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DiscountRepositoryCustom {

    Page<DiscountResponseDto> getItemDiscountList(Pageable pageable);

    List<DailySaleItemListDto> getDailySaleItems();
}
