package com.automate.vehicleservices.repository.dtoprojection;

import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.enums.FuelType;
import com.automate.vehicleservices.entity.enums.VehicleStatus;
import lombok.*;
import org.apache.commons.collections.CollectionUtils;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Chandrashekar V
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ServiceVehicleDTO {

    private int id;

    private String regNumber;

    private String tenantId;

    private LocalDate purchaseDate;

    private String vehicleModel;

    private FuelType fuelType;

    private String chassisNumber;

    private String vin;

    private VehicleStatus status;

    private String engineNumber;

    private String sellingLocation;

    private String sellingDealer;
    private String transmisionType;
    private String vehicleMake;
    private LocalDate vehicleMakeYear;

    private String variant;

    private String color;

    private Integer currentKmReading;

    private String makingMonth;

    private Integer makingYear;

    private Boolean isFastag;

    public ServiceVehicleDTO(final ServiceVehicle serviceVehicle) {
        if (Objects.isNull(serviceVehicle)) {
            new ServiceVehicleDTO();
            return;
        }

        this.purchaseDate = serviceVehicle.getPurchaseDate();
        this.tenantId = serviceVehicle.getMdTenant().getTenantIdentifier();
        this.regNumber = serviceVehicle.getRegNumber();
        this.id = serviceVehicle.getId();
        this.vehicleModel = serviceVehicle.getModel();
        this.fuelType = serviceVehicle.getFuelType();
        this.chassisNumber = serviceVehicle.getChassisNumber();
        this.vin = serviceVehicle.getVin();
        this.status = serviceVehicle.getStatus();
        this.engineNumber = serviceVehicle.getEngineNumber();
        this.color = serviceVehicle.getColor();
        this.variant = serviceVehicle.getVariant();
        this.sellingDealer = serviceVehicle.getSellingDealer();
        this.transmisionType=serviceVehicle.getTransmisionType();
        this.vehicleMake=serviceVehicle.getVehicleMake();
        this.vehicleMakeYear=serviceVehicle.getVehicleMakeYear();
        this.sellingLocation = serviceVehicle.getSellingLocation();
        this.currentKmReading = serviceVehicle.getCurrentKmReading();
        this.makingMonth = serviceVehicle.getMakingMonth();
        this.makingYear = serviceVehicle.getMakingYear();
        this.isFastag = serviceVehicle.getIsFastag();
    }

}
