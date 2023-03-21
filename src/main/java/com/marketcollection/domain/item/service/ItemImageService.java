package com.marketcollection.domain.item.service;

import com.marketcollection.common.unit.FileService;
import com.marketcollection.domain.common.LocalFileService;
import com.marketcollection.domain.item.dto.ItemImageDto;
import com.marketcollection.domain.item.ItemImage;
import com.marketcollection.domain.item.repository.ItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Transactional
@Service
public class ItemImageService {

    private final LocalFileService localFileService;
    public final ItemImageRepository itemImageRepository;

    // 상품 이미지 저장
    public void save(ItemImageDto itemImageDto, MultipartFile multipartFile) throws Exception {
        String originalFilename = multipartFile.getOriginalFilename();
        String renamedFileName = "";
        String itemImageUrl = "";

        if(!StringUtils.isEmpty(originalFilename)) {
            renamedFileName = localFileService.uploadFile(originalFilename, multipartFile);
            if (renamedFileName.startsWith("http")) {
                itemImageUrl = renamedFileName;
            } else {
                itemImageUrl = "/image/item/" + renamedFileName;
            }
        }

        itemImageDto.createItemImage(originalFilename, renamedFileName, itemImageUrl, false);
        ItemImage itemImage = itemImageDto.toEntity();
        itemImageRepository.save(itemImage);
    }

    // 썸네일 이미지 저장
    public ItemImage saveThumbnail(ItemImageDto itemImageDto, MultipartFile multipartFile) throws Exception {

        String originalFilename = multipartFile.getOriginalFilename();
        String renamedFileName = "";
        String itemImageUrl = "";

/*        if(!StringUtils.isEmpty(originalFilename)) {
            renamedFileName = localFileService.createThumbnailImage(originalFilename, multipartFile);
            itemImageUrl = "/image/item/" + renamedFileName;
        }*/

        itemImageDto.createItemImage(originalFilename, renamedFileName, itemImageUrl, true);
        ItemImage itemImage = itemImageDto.toEntity();
        itemImageRepository.save(itemImage);

        return itemImage;
    }

    // 상품 이미지 수정
    public void updateItemImage(Long itemImageId, MultipartFile multipartFile) throws Exception {
        if(!multipartFile.isEmpty()) {
            ItemImage itemImage = itemImageRepository.findById(itemImageId).orElseThrow(EntityNotFoundException::new);

/*            if(StringUtils.isEmpty(itemImage.getRenamedFileName())) {
                localFileService.deleteFile(itemImageLocation + "/" + itemImage.getRenamedFileName());
            }

            String originalFileName = multipartFile.getOriginalFilename();
            String renamedFileName = localFileService.uploadFile(originalFileName, multipartFile);
            String itemImageUrl ="/image/item/" + renamedFileName;
            itemImage.updateItemIamge(originalFileName, renamedFileName, itemImageUrl);*/
        }
    }

    // 썸네일 이미지 수정
    public ItemImage updateThumbnailImage(Long itemImageId, MultipartFile multipartFile) throws Exception {
        String originalFileName = "";
        String renamedFileName = "";
        String itemImageUrl = "";
        ItemImage thumbnailImage = null;
        if (!multipartFile.isEmpty()) {
            thumbnailImage = itemImageRepository.findById(itemImageId).orElseThrow(EntityNotFoundException::new);

            if (!StringUtils.isEmpty(thumbnailImage.getRenamedFileName())) {
//                localFileService.deleteFile(thumbnailImage.getRenamedFileName());
            }

            originalFileName = multipartFile.getOriginalFilename();
//            renamedFileName = localFileService.createThumbnailImage(originalFileName, multipartFile);
            itemImageUrl = "/image/item/" + renamedFileName;
            thumbnailImage.updateItemIamge(originalFileName, renamedFileName, itemImageUrl);
        }
        return thumbnailImage;
    }
}
