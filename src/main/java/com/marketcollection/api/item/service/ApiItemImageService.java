package com.marketcollection.api.item.service;

import com.marketcollection.api.common.FileService;
import com.marketcollection.api.item.dto.ItemFormDto;
import com.marketcollection.api.item.dto.ItemImageDto;
import com.marketcollection.domain.item.ItemImage;
import com.marketcollection.domain.item.repository.ItemImageRepository;
import com.marketcollection.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;

@RequiredArgsConstructor
@Transactional
@Service
public class ApiItemImageService {

    @Value("${itemImageLocation}")
    private String itemImageLocation;
    private final FileService fileService;
    public final ItemImageRepository itemImageRepository;

    public void save(ItemImageDto itemImageDto, MultipartFile itemImageFile) throws Exception {

        String originalFilename = itemImageFile.getOriginalFilename();
        String renamedFileName = "";
        String itemImageUrl = "";

        if(!StringUtils.isEmpty(originalFilename)) {
            renamedFileName = fileService.uploadFile(itemImageLocation, originalFilename, itemImageFile);
            itemImageUrl = "/images/items/" + renamedFileName;
        }

        itemImageDto.createItemImage(originalFilename, renamedFileName, itemImageUrl);
        System.out.println("getUrl = " + itemImageDto.getItemImageUrl());
        ItemImage itemImage = itemImageDto.toEntity();
        System.out.println("itemImage = " + itemImage.toString());
        itemImageRepository.save(itemImage);
    }

    public String createThumbnailImage(MultipartFile itemImageFile) throws Exception {

        String originalFilename = itemImageFile.getOriginalFilename();
        String thumbnailFileName = "";
        String thumbnailImageUrl = "";

        if(!StringUtils.isEmpty(originalFilename)) {
            thumbnailFileName = fileService.createThumbnailImage(itemImageLocation, originalFilename, itemImageFile);
            thumbnailImageUrl = "/images/items/" + thumbnailFileName;
        }

        return thumbnailImageUrl;
    }
}
