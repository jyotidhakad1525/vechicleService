package com.automate.vehicleservices.api.model.builder;

import com.automate.vehicleservices.api.model.DownloadReference;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Chandrashekar V
 */
public final class DownloadReferenceBuilder {
    private String url;
    private byte[] content;
    private String label;
    private String filename;
    private Map<String, Object> metadata = new HashMap<>();

    private DownloadReferenceBuilder() {
    }

    public static DownloadReferenceBuilder aDownloadReference() {
        return new DownloadReferenceBuilder();
    }

    public DownloadReferenceBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    public DownloadReferenceBuilder withContent(byte[] content) {
        this.content = content;
        return this;
    }

    public DownloadReferenceBuilder withLabel(String label) {
        this.label = label;
        return this;
    }

    public DownloadReferenceBuilder withFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public DownloadReferenceBuilder withMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
        return this;
    }

    public DownloadReference build() {
        DownloadReference downloadReference = new DownloadReference();
        downloadReference.setUrl(url);
        downloadReference.setContent(content);
        downloadReference.setLabel(label);
        downloadReference.setFilename(filename);
        downloadReference.setMetadata(metadata);
        return downloadReference;
    }
}
