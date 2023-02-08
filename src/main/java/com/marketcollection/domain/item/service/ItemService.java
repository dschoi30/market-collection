package com.marketcollection.domain.item.service;

import com.marketcollection.domain.item.dto.*;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.ItemImage;
import com.marketcollection.domain.item.repository.ItemImageRepository;
import com.marketcollection.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImageService itemImageService;
    private final ItemImageRepository itemImageRepository;

    public Long save(ItemFormDto itemFormDto, List<MultipartFile> itemImageFiles) throws Exception {
        Item item = itemFormDto.toEntity();
        itemRepository.save(item);

        ItemImageDto itemImageDto = new ItemImageDto();
        itemImageDto.setItem(item);

        for(int i = 0; i < itemImageFiles.size(); i++) {
            if(i == 0) {
                String thumbnailImage = itemImageService.createThumbnailImage(itemImageFiles.get(i));
                item.setThumbnailImageFile(thumbnailImage);
            } else {
                itemImageService.save(itemImageDto, itemImageFiles.get(i));
            }
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<ItemListDto> getItemListPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getItemListPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public ItemDetailDto getItemDetail(Long itemId) {
        List<ItemImage> itemImages = itemImageRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImageDto> itemImageDtos = new ArrayList<>();
        for(ItemImage itemImage : itemImages) {
            ItemImageDto itemImageDto = ItemImageDto.of(itemImage);
            itemImageDtos.add(itemImageDto);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemDetailDto itemDetailDto = ItemDetailDto.of(item);
        itemDetailDto.setItemImageDtos(itemImageDtos);

        return itemDetailDto;
    }
}
