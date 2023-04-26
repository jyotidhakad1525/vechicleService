package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.DocumentContent;
import com.automate.vehicleservices.entity.enums.SupportedFileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocContentDTO {
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


    public DocContentDTO(DocumentContent docContent) {
        if (Objects.isNull(docContent)) {
            new DocContentDTO();
            return;
        }
        this.id = docContent.getId();
        this.description = docContent.getDescription();
        this.label = docContent.getLabel();
        this.url = docContent.getUrl();
        this.uuid = docContent.getUuid();
        this.filename = docContent.getFilename();
        this.folder = docContent.getFolder();
        this.createdDate = docContent.getCreatedDate();
        this.fileType = docContent.getFileType();
    }
}
