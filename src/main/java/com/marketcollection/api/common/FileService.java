package com.marketcollection.api.common;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Slf4j
@Service
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, MultipartFile multipartFile) throws Exception {

        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String renamedFileName = uuid.toString() + extension;
        multipartFile.transferTo(new File(uploadPath, renamedFileName));
        return renamedFileName;
    }

    public String createThumbnailImage(String uploadPath, String originalFileName, MultipartFile multipartFile) throws Exception {

        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String thumbnailFileName = "t-" + uuid.toString() + extension;
        multipartFile.transferTo(new File(uploadPath, originalFileName));
        Thumbnails.of(new File(uploadPath, originalFileName))
                .forceSize(120, 120)
                .toFile(new File(uploadPath, thumbnailFileName));
        return thumbnailFileName;
    }

    public void deleteFile(String filePath) throws Exception {
        File file = new File(filePath);

        if(file.exists()) {
            file.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
