package com.automate.vehicleservices.service.cloud;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import com.automate.vehicleservices.api.model.DownloadReference;
import com.automate.vehicleservices.api.model.UploadReference;
import com.automate.vehicleservices.api.model.builder.DownloadReferenceBuilder;
import com.automate.vehicleservices.api.model.builder.UploadReferenceBuilder;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.service.FileStorageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static org.apache.commons.io.FileUtils.writeByteArrayToFile;

/**
 * Chandrashekar V
 */
@Service
public class S3FileStorageService implements FileStorageService {

    public static final int ONE_SEC = 3600;
    private static final Logger logger = LoggerFactory.getLogger(S3FileStorageService.class);
    @Value("${dms.vehicle-services.vehicle-images.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${dms.vehicle-services.vehicle-images.s3.pre-signed-url-expiration-in-hours}")
    private int preSignedURLExpiration;


    private AmazonS3 s3Client;

    @PostConstruct
    public void init() {
        s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    @Override
    public UploadReference store(byte[] bytes, String label, final String fileName) throws IOException {
        File file = convert(bytes, fileName);
        String key = generateFileName(file, label);
        PutObjectRequest putObjectRequest =
                new PutObjectRequest(bucket, key, file);
        putObjectRequest.setGeneralProgressListener(ProgressEvent::getBytesTransferred);
        Upload upload = TransferManagerBuilder.defaultTransferManager().upload(putObjectRequest);

        long totalBytesToTransfer = upload.getProgress().getTotalBytesToTransfer();
        long start = System.currentTimeMillis();
        while (upload.isDone()) {
            logger.info(String.format("Uploaded percentage: %f", upload.getProgress().getPercentTransferred()));

            if (System.currentTimeMillis() - start > ONE_SEC)
                throw new VehicleServicesException("Upload timeout exception. Try again.");
        }

        try {
            UploadResult uploadResult = upload.waitForUploadResult();
            return UploadReferenceBuilder.anUploadReference()
                    .withFolder(uploadResult.getBucketName())
                    .withFilename(uploadResult.getKey()).withLabel(label)
                    .withVersion(uploadResult.getVersionId()).build();
        } catch (InterruptedException e) {
            throw new VehicleServicesException("Failed while uploading the file");
        }
    }

    @Override
    public UploadReference store(MultipartFile file, String label) throws IOException {
        final String fileName = StringUtils.isBlank(file.getOriginalFilename()) ? file.getName()
                : file.getOriginalFilename();
        return store(file.getBytes(), label, fileName);

    }

    private File convert(byte[] data, final String fileName) throws IOException {
        File file = new File(fileName);
        writeByteArrayToFile(file, data);
        return file;
    }

    private String generateFileName(File file, final String label) {
        return new StringBuilder(LocalDateTime.now().toString()).append("-").
                append(cleanse(label)).append("-").append(cleanse(file.getName())).toString();
    }

    private String cleanse(String fileName) {
        if (StringUtils.isBlank(fileName))
            return fileName;

        return fileName.replace(StringUtils.SPACE, "_");
    }

    @Override
    public String storeAll(Map<String, MultipartFile> multipartFileMap) {
        return null;
    }


    @Override
    public DownloadReference load(String fileName) {

        S3ObjectId s3ObjectId = new S3ObjectIdBuilder().withBucket(bucket).withKey(fileName).withVersionId(null)
                .build();

        File file = createTempFile(fileName);
        GetObjectRequest getObjectRequest = new GetObjectRequest(s3ObjectId);
        getObjectRequest.setGeneralProgressListener(ProgressEvent::getBytesTransferred);
        Download download = TransferManagerBuilder.defaultTransferManager().download(getObjectRequest, file);

        while (download.isDone()) {
            logger.info(String.format("Download Percentage: %f", download.getProgress().getPercentTransferred()));
        }

        try {
            return DownloadReferenceBuilder.aDownloadReference().withFilename(download.getKey())
                    .withContent(Files.readAllBytes(file.toPath())).build();
        } catch (IOException e) {
            throw new VehicleServicesException("Exception while reading the file.");
        }

    }

    public File createTempFile(String fileName) {
        File file = null;
        try {
            file = File.createTempFile("prefix", fileName.substring(fileName.lastIndexOf(".") + 1));
        } catch (IOException e) {
            logger.error("Locally storing the file failed.");
            throw new VehicleServicesException("Unable to create file to store the downloaded content.");
        }
        return file;
    }

    @Override
    public URL loadURL(final String fileName) throws IOException {
        final LocalDateTime localDateTime = LocalDateTime.now().plusHours(preSignedURLExpiration);
        final Date expiration = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return s3Client.generatePresignedUrl(
                new GeneratePresignedUrlRequest(bucket, fileName).withMethod(HttpMethod.GET)
                        .withExpiration(expiration));
    }
}
