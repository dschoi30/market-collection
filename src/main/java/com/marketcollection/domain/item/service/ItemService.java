package com.marketcollection.domain.item.service;

import com.marketcollection.domain.common.PageCursor;
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
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImageService itemImageService;
    private final ItemImageRepository itemImageRepository;

    public static final Integer SMALL_SIZE = 500;
    public static final Integer REGULAR_SIZE = 800;

    // 상품 저장
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImageFiles) throws Exception {
        Item item = itemFormDto.toEntity();
        itemRepository.save(item);

        ItemImageDto itemImageDto = new ItemImageDto();
        itemImageDto.setItem(item);

        for(int i = 0; i < itemImageFiles.size(); i++) {

            if(i == 0) { // 업로드 파일 중 첫 번째 이미지 썸네일 저장
                ItemImage itemImage = itemImageService.save(itemImageDto, itemImageFiles.get(i), SMALL_SIZE);
                itemImage.setRepImage();
                item.addRepImageUrl(itemImage.getItemImageUrl());
            } else {
                itemImageService.save(itemImageDto, itemImageFiles.get(i), REGULAR_SIZE);
            }
        }

        return item.getId();
    }

    // 상품 목록 조회
    @Transactional(readOnly = true)
    public Page<ItemListDto> getItemListPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getItemListPage(itemSearchDto, pageable);
    }

    // 상품 상세 조회
    @Transactional(readOnly = true)
    public ItemDetailDto getItemDetail(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        List<ItemImage> itemImages = itemImageRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImageDto> itemImageDtos = itemImages.stream()
                .map(ItemImageDto::of)
                .collect(Collectors.toList());

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        boolean cookieExists = cookieExists(itemId, request, response); // 쿠키 존재 여부 확인
        if(!cookieExists) {
            item.addHit(); // 쿠키 미존재 시 조회수 증가
        }

        ItemDetailDto itemDetailDto = ItemDetailDto.of(item);
        itemDetailDto.setItemImageDtos(itemImageDtos);

        return itemDetailDto;
    }

    // 최근 본 상품 목록 조회
    @Transactional(readOnly = true)
    public List<ItemListDto> getRecentViewList(HttpServletRequest request) {

        Cookie cookie = findCookie(request.getCookies(), "hit");
        String hit = cookie.getValue();
        String[] cookieItemIds = hit.split("-");

        List<ItemListDto> itemListDtos = new ArrayList<>();
        List<Long> itemIds = new ArrayList<>();
        if(cookieItemIds.length > 1) {
            for(int i = cookieItemIds.length - 1; i >= 0; i--) {
                Long itemId = Long.parseLong(cookieItemIds[i]);
                itemIds.add(itemId);
            }
            itemListDtos = itemRepository.getRecentViewList(itemIds);
        }
        return itemListDtos;
    }

    public boolean cookieExists(Long itemId, HttpServletRequest request, HttpServletResponse response) {

        Cookie cookie = findCookie(request.getCookies(), "hit"); // hit 쿠키 조회
        String hit = cookie.getValue();
        boolean exist = false;

        if(hit != null && hit.contains(itemId + "-")) {
            exist = true;
        }

        if(!exist) {
            hit += itemId + "-";
            cookie.setValue(hit);
            cookie.setMaxAge(60 * 60 * 24);
            cookie.setPath("/");
            response.addCookie(cookie); // 쿠키 미존재 시 유효기간 1일의 hit 쿠키 생성
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

    // 관리자 상품 관리 페이지 조회
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    // 관리자 상품 수정 폼 반환
    @Transactional(readOnly = true)
    public ItemFormDto findById(Long itemId) {
        List<ItemImage> itemImages = itemImageRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImageDto> itemImageDtos = itemImages.stream()
                .map(ItemImageDto::of)
                .collect(Collectors.toList());

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImageDtos(itemImageDtos);

        return itemFormDto;
    }

    // 상품 정보 수정
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImageFiles) throws Exception {
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        List<Long> itemImageIds = itemFormDto.getItemImageIds();
        if(itemImageFiles != null) {
            for(int i = 0; i < itemImageFiles.size(); i++) {
                if(i == 0) {
                    ItemImage itemImage = itemImageService.updateItemImage(itemImageIds.get(i), itemImageFiles.get(i), SMALL_SIZE);
                    item.addRepImageUrl(itemImage.getItemImageUrl());
                } else {
                    itemImageService.updateItemImage(itemImageIds.get(i), itemImageFiles.get(i), REGULAR_SIZE);
                }
            }
        }
        return item.getId();
    }

    public List<Item> getItemList(List<Long> itemIds) {
        List<Item> items = new ArrayList<>();
        for(Long itemId : itemIds) {
            Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
            items.add(item);
        }

        return items;
    }
}
