package com.marketcollection.domain.item.repository;

import com.marketcollection.domain.item.ItemImage;

public interface ItemImageRepositoryCustom {

    ItemImage findByItemIdAndRepImageIsTrue(Long id, boolean isRepImage);
}
