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

    public void save(ItemImageDto itemImageDto, MultipartFile multipartFile) throws Exception {

        String originalFilename = multipartFile.getOriginalFilename();
        String renamedFileName = "";
        String itemImageUrl = "";

        if(!StringUtils.isEmpty(originalFilename)) {
            renamedFileName = fileService.uploadFile(itemImageLocation, originalFilename, multipartFile);
            itemImageUrl = "/image/item/" + renamedFileName;
        }

        itemImageDto.createItemImage(originalFilename, renamedFileName, itemImageUrl, false);
        ItemImage itemImage = itemImageDto.toEntity();
        itemImageRepository.save(itemImage);
    }


    public ItemImage saveThumbnail(ItemImageDto itemImageDto, MultipartFile multipartFile) throws Exception {

        String originalFilename = multipartFile.getOriginalFilename();
        String renamedFileName = "";
        String itemImageUrl = "";

        if(!StringUtils.isEmpty(originalFilename)) {
            renamedFileName = fileService.createThumbnailImage(itemImageLocation, originalFilename, multipartFile);
            itemImageUrl = "/image/item/" + renamedFileName;
        }

        itemImageDto.createItemImage(originalFilename, renamedFileName, itemImageUrl, true);
        ItemImage itemImage = itemImageDto.toEntity();
        itemImageRepository.save(itemImage);

        return itemImage;
    }


    public void updateItemImage(Long itemImageId, MultipartFile multipartFile) throws Exception {
        if(!multipartFile.isEmpty()) {
            ItemImage itemImage = itemImageRepository.findById(itemImageId).orElseThrow(EntityNotFoundException::new);

            if(StringUtils.isEmpty(itemImage.getRenamedFileName())) {
                fileService.deleteFile(itemImageLocation + "/" + itemImage.getRenamedFileName());
            }

            String originalFileName = multipartFile.getOriginalFilename();
            String renamedFileName = fileService.uploadFile(itemImageLocation, originalFileName, multipartFile);
            String itemImageUrl ="/image/item/" + renamedFileName;
            itemImage.updateItemIamge(originalFileName, renamedFileName, itemImageUrl);
        }

    }

    public void updateThumbnailImage(Long itemImageId, MultipartFile multipartFile) throws Exception {
        System.out.println("id===" + itemImageId);
        if(!multipartFile.isEmpty()) {
            ItemImage thumbnailImage = itemImageRepository.findById(itemImageId).orElseThrow(EntityNotFoundException::new);

            if(!StringUtils.isEmpty(thumbnailImage.getRenamedFileName())) {
                fileService.deleteFile(itemImageLocation + "/" + thumbnailImage.getRenamedFileName());
            }

            String originalFileName = multipartFile.getOriginalFilename();
            String renamedFileName = fileService.createThumbnailImage(itemImageLocation, originalFileName, multipartFile);
            String itemImageUrl ="/image/item/" + renamedFileName;
            thumbnailImage.updateItemIamge(originalFileName, renamedFileName, itemImageUrl);
        }
    }
}
