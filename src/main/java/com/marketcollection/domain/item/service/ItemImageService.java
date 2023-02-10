package com.marketcollection.domain.item.service;

import com.marketcollection.domain.common.FileService;
import com.marketcollection.domain.item.dto.ItemImageDto;
import com.marketcollection.domain.item.ItemImage;
import com.marketcollection.domain.item.repository.ItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Transactional
@Service
public class ItemImageService {

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
        ItemImage itemImage = itemImageDto.toEntity();
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


    public void updateItemImage(Long itemImageId, MultipartFile multipartFile) throws Exception {
        if(!multipartFile.isEmpty()) {
            ItemImage itemImage = itemImageRepository.findById(itemImageId).orElseThrow(EntityNotFoundException::new);

            if(StringUtils.isEmpty(itemImage.getRenamedFileName())) {
                fileService.deleteFile(itemImageLocation + "/" + itemImage.getRenamedFileName());
            }

            String originalFileName = itemImage.getOriginalFileName();
            String renamedFileName = fileService.uploadFile(itemImageLocation, originalFileName, multipartFile);
        }

    }

    public String updateThumbnailImage(MultipartFile itemImageFile, String thumbnailImageFile) throws Exception {
        String originalFilename = itemImageFile.getOriginalFilename();
        String thumbnailFileName = "";
        String thumbnailImageUrl = "";

        if(!StringUtils.isEmpty(originalFilename)) {
            fileService.deleteFile(itemImageLocation + "/" + thumbnailImageFile);
            thumbnailFileName = fileService.createThumbnailImage(itemImageLocation, originalFilename, itemImageFile);
            thumbnailImageUrl = "/images/items/" + thumbnailFileName;
        }

        return thumbnailImageUrl;

    }
}
