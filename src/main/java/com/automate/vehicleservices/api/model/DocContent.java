package com.automate.vehicleservices.api.model;

import com.automate.vehicleservices.entity.enums.SupportedFileType;
import com.automate.vehicleservices.framework.validation.ValidationRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author Chandrashekar V
 */
@Setter
@Getter
public class DocContent implements ValidationRequest {

    private String uuid;

    /**
     * base64 encoded file content.
     */
    private FileData data;

    private String documentURL;

    @NotBlank
    private String documentName;

    private LocalDate expiryDate;

    private String documentNumber;

    private String provider;

    private String nameOnTheDocument;

    private String description;

    private String label;

    private SupportedFileType fileType;

    @Getter
    @Setter
    public static class FileData {

        @NotNull
        private String base64;
        @NotBlank
        private String filename;

    }
}
