package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.api.model.DocContent;
import com.automate.vehicleservices.entity.enums.SupportedFileType;

import java.time.LocalDate;

public final class DocContentBuilder {
    private String uuid;
    private DocContent.FileData data;
    private String documentURL;
    private String documentName;
    private LocalDate expiryDate;
    private String documentNumber;
    private String provider;
    private String nameOnTheDocument;
    private String description;
    private String label;
    private SupportedFileType fileType;

    private DocContentBuilder() {
    }

    public static DocContentBuilder aDocContent() {
        return new DocContentBuilder();
    }

    public DocContentBuilder withUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public DocContentBuilder withData(DocContent.FileData data) {
        this.data = data;
        return this;
    }

    public DocContentBuilder withDocumentURL(String documentURL) {
        this.documentURL = documentURL;
        return this;
    }

    public DocContentBuilder withDocumentName(String documentName) {
        this.documentName = documentName;
        return this;
    }

    public DocContentBuilder withExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public DocContentBuilder withDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
        return this;
    }

    public DocContentBuilder withProvider(String provider) {
        this.provider = provider;
        return this;
    }

    public DocContentBuilder withNameOnTheDocument(String nameOnTheDocument) {
        this.nameOnTheDocument = nameOnTheDocument;
        return this;
    }

    public DocContentBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DocContentBuilder withLabel(String label) {
        this.label = label;
        return this;
    }

    public DocContentBuilder withFileType(SupportedFileType fileType) {
        this.fileType = fileType;
        return this;
    }

    public DocContent build() {
        DocContent docContent = new DocContent();
        docContent.setUuid(uuid);
        docContent.setData(data);
        docContent.setDocumentURL(documentURL);
        docContent.setDocumentName(documentName);
        docContent.setExpiryDate(expiryDate);
        docContent.setDocumentNumber(documentNumber);
        docContent.setProvider(provider);
        docContent.setNameOnTheDocument(nameOnTheDocument);
        docContent.setDescription(description);
        docContent.setLabel(label);
        docContent.setFileType(fileType);
        return docContent;
    }
}
