package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.entity.enums.SupportedFileType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * The persistent class for the document_content database table.
 */
@Entity
@Table(name = "document_content")
@NamedQuery(name = "DocumentContent.findAll", query = "SELECT d FROM DocumentContent d")
@Getter
@Setter
public class DocumentContent implements Serializable {
    private static final long serialVersionUID = -5116647549183922031L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(length = 500)
    private String description;

    @Lob
    @Column(name = "DOC_CONTENT")
    private byte[] docContent;

    @Column(name = "LABEL", length = 50)
    private String label;

    @Column(length = 1000)
    private String url;

    @Column(name = "UUID", length = 50)
    private String uuid;

    @Column(name = "FILE_NAME", length = 255)
    private String filename;

    @Column(name = "FOLDER", length = 255)
    private String folder;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "FILE_TYPE")
    private SupportedFileType fileType;

}