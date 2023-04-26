package com.automate.vehicleservices.api.model.builder;

import com.automate.vehicleservices.api.model.UploadReference;

/**
 * @author Chandrashekar V
 */
public final class UploadReferenceBuilder {
    private String folder;
    private String filename;
    private String version;
    private boolean status;
    private String label;

    private UploadReferenceBuilder() {
    }

    public static UploadReferenceBuilder anUploadReference() {
        return new UploadReferenceBuilder();
    }

    public UploadReferenceBuilder withFolder(String folder) {
        this.folder = folder;
        return this;
    }

    public UploadReferenceBuilder withFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public UploadReferenceBuilder withVersion(String version) {
        this.version = version;
        return this;
    }

    public UploadReferenceBuilder withStatus(boolean status) {
        this.status = status;
        return this;
    }

    public UploadReferenceBuilder withLabel(String label) {
        this.label = label;
        return this;
    }

    public UploadReferenceBuilder but() {
        return anUploadReference().withFolder(folder).withFilename(filename).withVersion(version).withStatus(status)
                .withLabel(label);
    }

    public UploadReference build() {
        UploadReference uploadReference = new UploadReference();
        uploadReference.setFolder(folder);
        uploadReference.setFilename(filename);
        uploadReference.setVersion(version);
        uploadReference.setStatus(status);
        uploadReference.setLabel(label);
        return uploadReference;
    }
}
