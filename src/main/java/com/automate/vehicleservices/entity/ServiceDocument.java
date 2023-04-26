package com.automate.vehicleservices.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * The persistent class for the service_documents database table.
 */
@Getter
@Setter
@Entity
@Table(name = "service_documents")
@NamedQuery(name = "ServiceDocument.findAll", query = "SELECT s FROM ServiceDocument s")
public class ServiceDocument implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "DOCUMENT_NUMBER", length = 100)
    private String documentNumber;

    @Column(name = "DOCUMENT_URL", length = 255)
    private String documentUrl;

    @Column(name = "EXPIRY_DATE")
    private LocalDate expiryDate;

    @Column(length = 1000)
    private String information;

    @Column(name = "NAME_ON_DOCUMENT", length = 255)
    private String nameOnDocument;

    @Column(length = 255)
    private String provider;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch =
            FetchType.LAZY)
    @JoinTable(name = "service_document_content", joinColumns = {@JoinColumn(name = "SERVICE_DOCUMENT_ID")},
            inverseJoinColumns = {@JoinColumn(name = "DOC_CONTENT_ID")})
    @JsonIgnore
    private List<DocumentContent> documentContents = new ArrayList<>();

    //bi-directional many-to-one association to Customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOCUMENT_TYPE")
    private MdVehicleDocumentType mdVehicleDocumentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VEHICLE_ID")
    private ServiceVehicle serviceVehicle;

    public DocumentContent addDocumentContent(DocumentContent documentContent) {
        getDocumentContents().add(documentContent);
        return documentContent;
    }

    public DocumentContent removeDocumentContent(DocumentContent documentContent) {
        getDocumentContents().remove(documentContent);
        return documentContent;
    }

    public List<DocumentContent> getDocumentContents() {
        if (Objects.isNull(documentContents))
            documentContents = new ArrayList<>();
        return documentContents;
    }
}