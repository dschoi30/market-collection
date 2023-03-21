package com.marketcollection.domain.item.service;

import com.marketcollection.common.unit.S3Uploader;
import com.marketcollection.domain.common.LocalFileService;
import com.marketcollection.domain.item.dto.ItemImageDto;
import com.marketcollection.domain.item.ItemImage;
import com.marketcollection.domain.item.repository.ItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Transactional
@Service
public class ItemImageService {

    private final S3Uploader s3Uploader;
    private final LocalFileService localFileService;
    private final ItemImageRepository itemImageRepository;

    @Value("${itemImageLocation}")
    private String uploadPath;

    // 상품 이미지 저장
    public ItemImage save(ItemImageDto itemImageDto, MultipartFile multipartFile, Integer imageSize) throws Exception {
        String originalFileName = multipartFile.getOriginalFilename();
        String renamedFileName = localFileService.uploadFile(uploadPath, originalFileName, multipartFile, imageSize);
        String uploadFilePath = s3Uploader.uploadFile(uploadPath + "/" + renamedFileName);

        itemImageDto.createItemImage(originalFileName, renamedFileName, uploadFilePath);
        ItemImage itemImage = itemImageDto.toEntity();
        itemImageRepository.save(itemImage);

        return itemImage;
    }

    // 상품 이미지 수정
    public ItemImage updateItemImage(Long itemImageId, MultipartFile multipartFile, Integer imageSize) throws Exception {
        ItemImage itemImage = itemImageRepository.findById(itemImageId).orElseThrow(EntityNotFoundException::new);
        s3Uploader.removeS3Files(itemImage.getRenamedFileName());

        String originalFileName = multipartFile.getOriginalFilename();
        String renamedFileName = localFileService.uploadFile(uploadPath, originalFileName, multipartFile, imageSize);
        String uploadFilePath = s3Uploader.uploadFile(uploadPath + "/" + renamedFileName);

        itemImage.updateItemIamge(originalFileName, renamedFileName, uploadFilePath);

        return itemImage;
    }
}
