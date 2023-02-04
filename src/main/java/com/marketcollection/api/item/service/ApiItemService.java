package com.marketcollection.api.item.service;

import com.marketcollection.api.item.dto.ItemImageDto;
import com.marketcollection.domain.item.Item;
import com.marketcollection.api.item.dto.ItemFormDto;
import com.marketcollection.domain.item.ItemImage;
import com.marketcollection.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ApiItemService {

    private final ApiItemImageService apiItemImageService;
    private final ItemRepository itemRepository;

    public Long save(ItemFormDto itemFormDto, List<MultipartFile> itemImageFiles) throws Exception {

        ItemFormDto updatedItemFormDto = apiItemImageService.createThumbnailImage(itemFormDto, itemImageFiles.get(0));
        Item item = updatedItemFormDto.toEntity();
        itemRepository.save(item);
        ItemImageDto itemImageDto = new ItemImageDto();
        itemImageDto.setItem(item);

        for(int i = 0; i < itemImageFiles.size(); i++) {
            apiItemImageService.save(itemImageDto, itemImageFiles.get(i));
        }

        return item.getId();
    }
}
