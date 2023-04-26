package com.automate.vehicleservices.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * The persistent class for the md_vehicle_document_type database table.
 */
@Setter
@Getter
@Entity
@Table(name = "md_vehicle_document_type")
@NamedQuery(name = "MdVehicleDocumentType.findAll", query = "SELECT m FROM MdVehicleDocumentType m")
public class MdVehicleDocumentType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "CREATED_BY", length = 255)
    private String createdBy;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(length = 500)
    private String description;

    @Column(name = "DOCUMENT_NAME", nullable = false, length = 255)
    private String documentName;

    @Column(name = "LABEL", nullable = false, length = 50)
    private String label;

    @Column(name = "LAST_MODIFIED_DATE")
    private LocalDateTime lastModifiedDate;

    @Column(name = "MODIFIED_BY", length = 255)
    private String modifiedBy;

    //bi-directional many-to-one association to MdTenant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID", nullable = false)
    @JsonBackReference
    private MdTenant mdTenant;

}