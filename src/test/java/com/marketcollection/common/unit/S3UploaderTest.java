package com.marketcollection.common.unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class S3UploaderTest {

    @Autowired
    private S3Uploader s3Uploader;

    @Test
    public void uploadTest() throws IOException {

//        String uploadName = s3Uploader.uploadFile(originalFileName, multipartFile);
    }
}