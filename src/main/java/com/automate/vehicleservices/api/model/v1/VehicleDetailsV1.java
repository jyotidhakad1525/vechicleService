package com.automate.vehicleservices.api.model.v1;

import com.automate.vehicleservices.entity.enums.FuelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VehicleDetailsV1 {

    private Integer id;

    @NotBlank
    private String vehicleRegNumber;

    private String vehicleMake;

    private String vehicleModel;

    private String variant;

    private String color;

    private FuelType fuelType;

    private String transmisionType;

    private String vin;

    private String engineNumber;

    @PositiveOrZero
    private Integer currentKmReading;

    @NotNull
    @PastOrPresent
    private LocalDate purchaseDate;

    @NotBlank
    private String makingMonth;

    @NotNull
    private Integer makingYear;

    private String chassisNumber;

    private String sellingDealer;

    private String sellingLocation;

    private Boolean isFastag;
}
