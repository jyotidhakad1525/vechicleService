package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.ServiceDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ServiceDocumentDTO {
    private String documentNumber;
    private String documentUrl;
    private LocalDate expiryDate;
    private String information;
    private String nameOnDocument;
    private String provider;
    private String customer;
    private MdVehicleDocumentTypeDTO mdVehicleDocumentType;
    private String vehicleRegNumber;
    private List<DocContentDTO> contents;


    public ServiceDocumentDTO(ServiceDocument serviceDocument) {

        if (Objects.isNull(serviceDocument)) {
            new ServiceDocument();
            return;
        }

        this.documentNumber = serviceDocument.getDocumentNumber();
        this.documentUrl = serviceDocument.getDocumentUrl();
        this.expiryDate = serviceDocument.getExpiryDate();
        this.information = serviceDocument.getInformation();
        this.nameOnDocument = serviceDocument.getNameOnDocument();
        this.provider = serviceDocument.getProvider();
        final var customer = serviceDocument.getCustomer();
        if (Objects.nonNull(customer))
            this.customer = customer.getFirstName();
        this.mdVehicleDocumentType = new MdVehicleDocumentTypeDTO(serviceDocument.getMdVehicleDocumentType());
        final var serviceVehicle = serviceDocument.getServiceVehicle();
        if (Objects.nonNull(serviceVehicle))
            this.vehicleRegNumber = serviceVehicle.getRegNumber();

        final var documentContents = serviceDocument.getDocumentContents();
        if (CollectionUtils.isNotEmpty(documentContents)) {
            this.contents = documentContents.stream().map(DocContentDTO::new).collect(Collectors.toList());
        }
    }
}
