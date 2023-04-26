package com.automate.vehicleservices.service.cloud;

import com.automate.vehicleservices.repository.BaseTest;
import com.automate.vehicleservices.service.FileStorageService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class S3FileStorageServiceTest extends BaseTest {

    private static String fileName;
    @Autowired
    FileStorageService storageService;

    @Test
    @Order(1)
    void store() throws IOException {

        MockMultipartFile mockMultipartFile = new MockMultipartFile(System.currentTimeMillis() + "_test.txt",
                "test".getBytes());
        final var uploadReference = storageService.store(mockMultipartFile, "vehicle_image_upload_test");

        System.out.println("S3 Object id:" + uploadReference.getFilename());

        fileName = uploadReference.getFilename();

        assertNotNull(fileName);
    }

    @Test
    @Order(2)
    void load() throws IOException {
        final var downloadReference = storageService.load(fileName);
        System.out.println(downloadReference.getFilename());
        assertNotNull(downloadReference);
    }

    @Test
    @Order(3)
    void loadURL() throws IOException {
        final var url = storageService.loadURL(fileName);
        System.out.println(url.toString());
        assertNotNull(url);
    }
}