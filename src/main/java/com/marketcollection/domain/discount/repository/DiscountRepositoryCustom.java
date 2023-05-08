package com.marketcollection.domain.discount.repository;

import com.marketcollection.domain.discount.dto.DiscountResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiscountRepositoryCustom {

    Page<DiscountResponseDto> getItemDiscountList(Pageable pageable);
}
