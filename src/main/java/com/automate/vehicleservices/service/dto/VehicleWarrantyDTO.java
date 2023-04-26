package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.VehicleWarranty;
import com.automate.vehicleservices.entity.enums.WarrantyStatus;
import com.automate.vehicleservices.entity.enums.WarrantyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleWarrantyDTO {
    private int id;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private WarrantyStatus status;
    private double amountPaid;
    private WarrantyType warrantyType;
    private String tenantIdentifier;
    private String vehicleRegNumber;
    private String oemPeriod;
    private String ewName;
    private String amc_name;
    private String fastagStatus;
    public VehicleWarrantyDTO(VehicleWarranty vehicleWarranty) {
        if (Objects.isNull(vehicleWarranty)) {
            new VehicleWarrantyDTO();
            return;
        }

        this.id = vehicleWarranty.getId();
        this.startDate = vehicleWarranty.getStartDate();
        this.expiryDate = vehicleWarranty.getExpiryDate();
        this.status = vehicleWarranty.getStatus();
        this.amountPaid = Optional.ofNullable(vehicleWarranty.getAmountPaid()).orElse(0d);
        this.warrantyType = vehicleWarranty.getWarrantyTpe();
        this.amc_name = vehicleWarranty.getAmc_name();
        this.ewName = vehicleWarranty.getEwName();
        this.fastagStatus = vehicleWarranty.getFastagStatus();
        this.oemPeriod = vehicleWarranty.getOemPeriod();
        final var mdTenant = vehicleWarranty.getMdTenant();
        if (Objects.nonNull(mdTenant))
            this.tenantIdentifier = mdTenant.getTenantIdentifier();
        final var serviceVehicle = vehicleWarranty.getServiceVehicle();
        if (Objects.nonNull(serviceVehicle))
            this.vehicleRegNumber = serviceVehicle.getRegNumber();
    }
}
