package com.marketcollection.common.unit;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    public String uploadFile(String originalFileName, MultipartFile multipartFile) throws IOException;
}
