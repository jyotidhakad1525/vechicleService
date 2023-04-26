package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class ServiceDocumentBuilder {
    private int id;
    private String documentNumber;
    private String documentUrl;
    private LocalDate expiryDate;
    private String information;
    private String nameOnDocument;
    private String provider;
    private final List<DocumentContent> documentContents = new ArrayList<>();
    private Customer customer;
    private MdVehicleDocumentType mdVehicleDocumentType;
    private ServiceVehicle serviceVehicle;

    private ServiceDocumentBuilder() {
    }

    public static ServiceDocumentBuilder aServiceDocument() {
        return new ServiceDocumentBuilder();
    }

    public ServiceDocumentBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public ServiceDocumentBuilder withDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
        return this;
    }

    public ServiceDocumentBuilder withDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
        return this;
    }

    public ServiceDocumentBuilder withExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public ServiceDocumentBuilder withInformation(String information) {
        this.information = information;
        return this;
    }

    public ServiceDocumentBuilder withNameOnDocument(String nameOnDocument) {
        this.nameOnDocument = nameOnDocument;
        return this;
    }

    public ServiceDocumentBuilder withProvider(String provider) {
        this.provider = provider;
        return this;
    }

    public ServiceDocumentBuilder withDocumentContent(DocumentContent documentContent) {
        this.documentContents.add(documentContent);
        return this;
    }

    public ServiceDocumentBuilder withCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public ServiceDocumentBuilder withMdVehicleDocumentType(MdVehicleDocumentType mdVehicleDocumentType) {
        this.mdVehicleDocumentType = mdVehicleDocumentType;
        return this;
    }

    public ServiceDocumentBuilder withServiceVehicle(ServiceVehicle serviceVehicle) {
        this.serviceVehicle = serviceVehicle;
        return this;
    }

    public ServiceDocument build() {
        ServiceDocument serviceDocument = new ServiceDocument();
        serviceDocument.setId(id);
        serviceDocument.setDocumentNumber(documentNumber);
        serviceDocument.setDocumentUrl(documentUrl);
        serviceDocument.setExpiryDate(expiryDate);
        serviceDocument.setInformation(information);
        serviceDocument.setNameOnDocument(nameOnDocument);
        serviceDocument.setProvider(provider);
        serviceDocument.setDocumentContents(documentContents);
        serviceDocument.setCustomer(customer);
        serviceDocument.setMdVehicleDocumentType(mdVehicleDocumentType);
        serviceDocument.setServiceVehicle(serviceVehicle);
        return serviceDocument;
    }
}
