package com.marketcollection.domain.item.dto;

import com.marketcollection.domain.item.ItemSaleStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchDto {

    private String searchDateType;
    private ItemSaleStatus itemSaleStatus;
    private String searchBy;
    private String searchQuery = "";
}
