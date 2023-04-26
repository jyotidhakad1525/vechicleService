package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.DocumentContent;
import com.automate.vehicleservices.entity.enums.SupportedFileType;

import java.time.LocalDateTime;

public final class DocumentContentBuilder {
    private int id;
    private String description;
    private byte[] docContent;
    private String label;
    private String url;
    private String uuid;
    private String filename;
    private String folder;
    private LocalDateTime createdDate;
    private SupportedFileType fileType;

    private DocumentContentBuilder() {
    }

    public static DocumentContentBuilder aDocumentContent() {
        return new DocumentContentBuilder();
    }

    public DocumentContentBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public DocumentContentBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DocumentContentBuilder withDocContent(byte[] docContent) {
        this.docContent = docContent;
        return this;
    }

    public DocumentContentBuilder withLabel(String label) {
        this.label = label;
        return this;
    }

    public DocumentContentBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    public DocumentContentBuilder withUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public DocumentContentBuilder withFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public DocumentContentBuilder withFolder(String folder) {
        this.folder = folder;
        return this;
    }

    public DocumentContentBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public DocumentContentBuilder withFileType(SupportedFileType fileType) {
        this.fileType = fileType;
        return this;
    }

    public DocumentContent build() {
        DocumentContent documentContent = new DocumentContent();
        documentContent.setId(id);
        documentContent.setDescription(description);
        documentContent.setDocContent(docContent);
        documentContent.setLabel(label);
        documentContent.setUrl(url);
        documentContent.setUuid(uuid);
        documentContent.setFilename(filename);
        documentContent.setFolder(folder);
        documentContent.setCreatedDate(createdDate);
        documentContent.setFileType(fileType);
        return documentContent;
    }
}
