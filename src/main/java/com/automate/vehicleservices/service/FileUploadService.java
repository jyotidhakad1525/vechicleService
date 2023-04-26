package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.UploadReference;
import com.automate.vehicleservices.entity.DocumentContent;
import com.automate.vehicleservices.entity.builder.DocumentContentBuilder;
import com.automate.vehicleservices.entity.enums.SupportedFileType;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.framework.service.CrudService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.UUID;

/**
 * Chandrashekar V
 */
@Service
public class FileUploadService {

    private final FileStorageService storageService;

    private final CrudService crudService;

    public FileUploadService(FileStorageService storageService, CrudService crudService) {
        this.storageService = storageService;
        this.crudService = crudService;
    }

    public String upload(MultipartFile file, String label) throws InterruptedIOException {

        try {
            final var uploadReference = storageService.store(file, label);

            final var persist = persist(uploadReference);
            return persist.getUuid();

        } catch (IOException e) {
            throw new InterruptedIOException(String.format("Upload failed for file :%s", file.getName()));
        }
    }

    public String upload(byte[] file, String label, final String fileName,
                         final String fileType) throws InterruptedIOException {

        try {
            final var uploadReference = storageService.store(file, label, fileName);
            final var persist = persist(uploadReference);
            return persist.getUuid();

        } catch (IOException e) {
            throw new InterruptedIOException(String.format("Upload failed for file :%s", fileName));
        }
    }

    /**
     * Store it to the database. later the same will be referenced to respective vehicle document.
     *
     * @param uploadReference
     * @return
     */
    private DocumentContent persist(UploadReference uploadReference) {

        final var documentContent = DocumentContentBuilder.aDocumentContent()
                .withFilename(uploadReference.getFilename())
                .withFolder(uploadReference.getFolder())
                .withFileType(getFileExtension(uploadReference.getFilename()))
                .withLabel(uploadReference.getLabel()).withUuid(UUID.randomUUID().toString()).build();

        return crudService.save(documentContent);
    }

    /**
     * Get file extension from file type.
     *
     * @param filename
     * @return
     */
    private SupportedFileType getFileExtension(String filename) {

        if (StringUtils.isBlank(filename))
            throw new VehicleServicesException("Un supported file type. Specify the file extension");

        final var i = filename.lastIndexOf(".");

        if (i == -1)
            throw new VehicleServicesException("File type doesnt exist");

        final var fileType = filename.substring(i + 1);

        final var supportedFileType =
                SupportedFileType.fetchEnumType(fileType);


        return supportedFileType.orElseThrow(() -> new VehicleServicesException(String.format(
                "Unsupported file type. %s", fileType)));

    }
}
