package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.DocContent;
import com.automate.vehicleservices.entity.DocumentContent;
import com.automate.vehicleservices.entity.builder.DocumentContentBuilder;
import com.automate.vehicleservices.repository.DocumentContentRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.InterruptedIOException;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * Chandrashekar V
 */
@Service
@Slf4j
public class DocumentContentService {

    private final DocumentContentRepository documentContentRepository;
    private final FileUploadService fileUploadService;

    public DocumentContentService(DocumentContentRepository documentContentRepository,
                                  FileUploadService fileUploadService) {
        this.documentContentRepository = documentContentRepository;
        this.fileUploadService = fileUploadService;
    }

    public DocumentContent findByUUID(final String uuid) {
        DocumentContent documentContent = documentContentRepository.findByUuid(uuid);
        return documentContent;
    }

    public DocumentContent constructDocumentContentEntityFromURL(DocContent docContent) {
        return DocumentContentBuilder.aDocumentContent()
                .withFilename(fileNameFromURL(docContent.getDocumentURL()))
                .withFileType(docContent.getFileType())
                .withLabel(docContent.getLabel())
                .withCreatedDate(LocalDateTime.now())
                .withDescription(docContent.getDescription())
                .withUrl(docContent.getDocumentURL())
                .build();
    }

    private String fileNameFromURL(final String url) {
        if (StringUtils.isNoneBlank(url)) {
            int fwdSlashLastIndexInURL = url.lastIndexOf("/");
            if (fwdSlashLastIndexInURL != -1)
                return url.substring(fwdSlashLastIndexInURL);
        }
        return StringUtils.EMPTY;
    }

    /**
     * Decode the base64 encoded file, upload to storage and then return the entity.
     *
     * @param docContent
     * @return
     */
    public DocumentContent constructDocumentContentEntityFromEncodedString(DocContent docContent) {

        if (StringUtils.isBlank(docContent.getData().getBase64()))
            return null;

        try {
            String uuid = fileUploadService.upload(decode(docContent.getData().getBase64()),
                    docContent.getLabel(),
                    docContent.getData().getFilename(),
                    docContent.getFileType().name().toLowerCase());
            return findByUUID(uuid);
        } catch (InterruptedIOException e) {
            log.error("Exception while uploading the document.");
        }
        return null;
    }

    public byte[] decode(final String encodedString) {
        return Base64.getDecoder().decode(encodedString.getBytes());
    }
}
