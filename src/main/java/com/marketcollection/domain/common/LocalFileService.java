package com.marketcollection.domain.common;

import com.marketcollection.common.unit.FileService;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class LocalFileService {

    @Value("${itemImageLocation}")
    private String uploadPath;

    public String uploadFile(String originalFileName, MultipartFile multipartFile) throws IOException {

        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String renamedFileName = uuid.toString() + extension;
        multipartFile.transferTo(new File(uploadPath, renamedFileName));
        return renamedFileName;
    }

    public String createThumbnailImage(String originalFileName, MultipartFile multipartFile) throws IOException {

        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String renamedFileName = "t-" + uuid.toString() + extension;
        multipartFile.transferTo(new File(uploadPath, renamedFileName));
        Thumbnails.of(new File(uploadPath, renamedFileName))
                .forceSize(500, 500)
                .toFile(new File(uploadPath, renamedFileName));
        return renamedFileName;
    }

    public void deleteFile(String filePath) throws IOException {
        File file = new File(uploadPath + "/" + filePath);

        if(file.exists()) {
            file.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
