package com.automate.vehicleservices.repository.dtoprojection;

import com.automate.vehicleservices.entity.*;
import com.automate.vehicleservices.entity.enums.ServiceGroup;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Chandrashekar V
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleServiceHistoryDTO {
    private int kmReading;
    private LocalDate serviceDate;
    private int serviceTypeId;
    private String tenantIdentifier;
    private String vehicleRegNumber;
    private String categoryName;
    private ServiceGroup serviceGroup;
    private String subServiceType;
    private Float serviceAmount;
    private String serviceCenter;
    private String dealerName;

    private String roNumber;

    private LocalDate roDate;


    public VehicleServiceHistoryDTO(VehicleServiceHistory vehicleServiceHistory) {
        this.kmReading = vehicleServiceHistory.getKmReading();
        this.serviceDate = vehicleServiceHistory.getServiceDate();
        this.serviceAmount = vehicleServiceHistory.getServiceAmount();
        this.serviceCenter = vehicleServiceHistory.getServiceCenter();
        this.dealerName = vehicleServiceHistory.getDealerName();
        MdTenant mdTenant = vehicleServiceHistory.getMdTenant();
        if (Objects.nonNull(mdTenant))
            this.tenantIdentifier = mdTenant.getTenantIdentifier();
        ServiceVehicle serviceVehicle = vehicleServiceHistory.getServiceVehicle();
        if (Objects.nonNull(serviceVehicle))
            this.vehicleRegNumber = serviceVehicle.getRegNumber();
        MdServiceType mdServiceType = vehicleServiceHistory.getMdServiceType();
        if (Objects.nonNull(mdServiceType)) {
            this.serviceTypeId = mdServiceType.getId();
            this.subServiceType = mdServiceType.getServiceName();
            MdServiceCategory mdServiceCategory = mdServiceType.getMdServiceCategory();
            if (Objects.nonNull(mdServiceCategory)) {
                this.categoryName = mdServiceCategory.getCategoryName();
                this.serviceGroup = mdServiceCategory.getServiceGroup();
            }
        }

    }
}
