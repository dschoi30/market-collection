package com.marketcollection.domain.common;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static com.marketcollection.domain.item.service.ItemService.SMALL_SIZE;

@Service
public class LocalFileService {

    public String uploadFile(String uploadPath, String originalFileName, MultipartFile multipartFile, Integer imageSize) throws IOException {
        UUID uuid = UUID.randomUUID();
        String renamedFileName = "";
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        if(Objects.equals(imageSize, SMALL_SIZE)) {
            renamedFileName = "s-" + uuid.toString() + extension;
            multipartFile.transferTo(new File(uploadPath, renamedFileName));
            Thumbnails.of(new File(uploadPath, renamedFileName))
                    .forceSize(SMALL_SIZE, SMALL_SIZE)
                    .toFile(new File(uploadPath, renamedFileName));
        } else {
            renamedFileName = uuid.toString() + extension;
            multipartFile.transferTo(new File(uploadPath, renamedFileName));
            Thumbnails.of(new File(uploadPath, renamedFileName))
                    .size(imageSize, imageSize)
                    .toFile(new File(uploadPath, renamedFileName));
        }

        return renamedFileName;
    }
}
