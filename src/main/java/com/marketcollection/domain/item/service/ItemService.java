package com.marketcollection.domain.item.service;

import com.marketcollection.domain.item.dto.*;
import com.marketcollection.domain.item.Item;
import com.marketcollection.domain.item.ItemImage;
import com.marketcollection.domain.item.repository.ItemImageRepository;
import com.marketcollection.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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
                ItemImage itemImage = itemImageService.saveThumbnail(itemImageDto, itemImageFiles.get(i));
                itemImage.setRepImage();
                item.addRepImageUrl(itemImage.getItemImageUrl());
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
    public ItemDetailDto getItemDetail(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        List<ItemImage> itemImages = itemImageRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImageDto> itemImageDtos = new ArrayList<>();
        for(ItemImage itemImage : itemImages) {
            ItemImageDto itemImageDto = ItemImageDto.of(itemImage);
            itemImageDtos.add(itemImageDto);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        boolean cookieExists = cookieExists(itemId, request, response);
        if(!cookieExists) {
            item.addHit();
        }

        ItemDetailDto itemDetailDto = ItemDetailDto.of(item);
        itemDetailDto.setItemImageDtos(itemImageDtos);

        return itemDetailDto;
    }

    public boolean cookieExists(Long itemId, HttpServletRequest request, HttpServletResponse response) {

        Cookie cookie = findCookie(request.getCookies(), "hit");
        String hit = cookie.getValue();
        boolean exist = false;

        log.info("[[cookieName]]=" + cookie.getName());
        log.info("[[cookieValue]]=" + cookie.getValue());
        if(hit != null && hit.indexOf(itemId + "-") >= 0) {
            exist = true;
        }

        if(!exist) {
            hit += itemId + "-";
            cookie.setValue(hit);
            cookie.setMaxAge(60 * 60 * 24);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return exist;
    }

    private Cookie findCookie(Cookie[] cookies, String cookieName) {
        Cookie targetCookie = null;

        if(cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(cookieName)) {
                    targetCookie = cookie;
                    break;
                }
            }
        }

        if(targetCookie == null) {
            targetCookie = new Cookie(cookieName,"");
            targetCookie.setPath("/");
            targetCookie.setMaxAge(60 * 60 * 24);
        }
        return targetCookie;
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public ItemFormDto findById(Long itemId) {
        List<ItemImage> itemImages = itemImageRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImageDto> itemImageDtos = new ArrayList<>();
        for(ItemImage itemImage : itemImages) {
            ItemImageDto itemImageDto = ItemImageDto.of(itemImage);
            itemImageDtos.add(itemImageDto);
        }
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImageDtos(itemImageDtos);

        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImageFiles) throws Exception {
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        List<Long> itemImageIds = itemFormDto.getItemImageIds();

        for(int i = 0; i < itemImageIds.size(); i++) {
            if(i == 0) {
               itemImageService.updateThumbnailImage(itemImageIds.get(i), itemImageFiles.get(i));
            } else {
                itemImageService.updateItemImage(itemImageIds.get(i), itemImageFiles.get(i));
            }
        }
        return item.getId();
    }
}
