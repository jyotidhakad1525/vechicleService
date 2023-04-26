package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.DownloadReference;
import com.automate.vehicleservices.api.model.UploadReference;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * @author Chandrashekar V
 */
public interface FileStorageService {

    UploadReference store(byte[] file, String label, String fileName) throws IOException;

    UploadReference store(MultipartFile file, String label) throws IOException;

    String storeAll(Map<String, MultipartFile> multipartFileMap);

    DownloadReference load(String fileName) throws IOException;

    URL loadURL(String fileName) throws IOException;

}
