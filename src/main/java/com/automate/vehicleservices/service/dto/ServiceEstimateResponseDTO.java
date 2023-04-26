package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.ServiceEstimate;
import com.automate.vehicleservices.entity.enums.ServiceGroup;
import com.automate.vehicleservices.repository.dtoprojection.RateCardItem;
import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import lombok.*;
import org.apache.commons.collections.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Chandrashekar V
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEstimateResponseDTO {

    private String tenant;

    private String organization;

    private ServiceGroup serviceGroup;

    private ServiceVehicleDTO serviceVehicle;

    private LocalDateTime requestedDate;

    private CustomerDTO customer;

    private Map<String, List<RateCardItem>> estimateResponse;

    private double total;

    private double additionalServicesTotal;

    private List<ServiceDocumentDTO> serviceDocuments;

    public ServiceEstimateResponseDTO(ServiceEstimate serviceEstimate) {
        if (Objects.isNull(serviceEstimate))
            return;
        this.serviceGroup = serviceEstimate.getServiceGroup();
        this.requestedDate = serviceEstimate.getCreatedDate();
        this.customer = new CustomerDTO(serviceEstimate.getCustomer());
        this.serviceVehicle = new ServiceVehicleDTO(serviceEstimate.getServiceVehicle());
        tenantInfo(serviceEstimate);
        estimateResponse(serviceEstimate);
        serviceDocs(serviceEstimate);
    }

    private void estimateResponse(ServiceEstimate serviceEstimate) {
        final var serviceEstimateResponse = serviceEstimate.getServiceEstimateResponse();

        if (Objects.nonNull(serviceEstimateResponse)) {
            this.total = Optional.ofNullable(serviceEstimateResponse.getTotal()).orElse(0d);
            this.additionalServicesTotal =
                    Optional.ofNullable(serviceEstimateResponse.getAdditionalServicesTotal()).orElse(0d);
            this.estimateResponse = serviceEstimateResponse.getEstimation();
        }
    }

    private void serviceDocs(ServiceEstimate serviceEstimate) {
        final var documents = serviceEstimate.getServiceDocuments();
        if (CollectionUtils.isNotEmpty(documents))
            this.serviceDocuments = documents.stream().map(ServiceDocumentDTO::new).collect(Collectors.toList());
    }

    private void tenantInfo(ServiceEstimate serviceEstimate) {
        final var mdTenant = serviceEstimate.getMdTenant();
        if (Objects.nonNull(mdTenant)) {
            this.tenant = mdTenant.getTenantIdentifier();
            orgInfo(mdTenant);
        }
    }

    private void orgInfo(MdTenant mdTenant) {
        final var mdOrganization = mdTenant.getMdOrganization();
        if (Objects.nonNull(mdOrganization))
            this.organization = mdOrganization.getOrgName();
    }
}
