package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.MdVehicleDocumentType;

import java.time.LocalDateTime;

/**
 * Chandrashekar V
 */
public final class MdVehicleDocumentTypeBuilder {
    private String createdBy;
    private LocalDateTime createdDate;
    private String description;
    private String documentName;
    private String label;
    private LocalDateTime lastModifiedDate;
    private String modifiedBy;
    //bi-directional many-to-one association to MdTenant
    private MdTenant mdTenant;

    private MdVehicleDocumentTypeBuilder() {
    }

    public static MdVehicleDocumentTypeBuilder aMdVehicleDocumentType() {
        return new MdVehicleDocumentTypeBuilder();
    }

    public MdVehicleDocumentTypeBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public MdVehicleDocumentTypeBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public MdVehicleDocumentTypeBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public MdVehicleDocumentTypeBuilder withDocumentName(String documentName) {
        this.documentName = documentName;
        return this;
    }

    public MdVehicleDocumentTypeBuilder withLabel(String label) {
        this.label = label;
        return this;
    }

    public MdVehicleDocumentTypeBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public MdVehicleDocumentTypeBuilder withModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    public MdVehicleDocumentTypeBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public MdVehicleDocumentTypeBuilder but() {
        return aMdVehicleDocumentType().withCreatedBy(createdBy).withCreatedDate(createdDate)
                .withDescription(description).withDocumentName(documentName).withLabel(label)
                .withLastModifiedDate(lastModifiedDate).withModifiedBy(modifiedBy).withMdTenant(mdTenant);
    }

    public MdVehicleDocumentType build() {
        MdVehicleDocumentType mdVehicleDocumentType = new MdVehicleDocumentType();
        mdVehicleDocumentType.setCreatedBy(createdBy);
        mdVehicleDocumentType.setCreatedDate(createdDate);
        mdVehicleDocumentType.setDescription(description);
        mdVehicleDocumentType.setDocumentName(documentName);
        mdVehicleDocumentType.setLabel(label);
        mdVehicleDocumentType.setLastModifiedDate(lastModifiedDate);
        mdVehicleDocumentType.setModifiedBy(modifiedBy);
        mdVehicleDocumentType.setMdTenant(mdTenant);
        return mdVehicleDocumentType;
    }
}
