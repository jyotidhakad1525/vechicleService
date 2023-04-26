package com.automate.vehicleservices.api.model;

import com.automate.vehicleservices.entity.enums.FuelType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

/**
 * @author Chandrashekar V
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VehicleDetails {

    @NotBlank
    private String vehicleRegNumber;

    private String vin;

    private String engineNumber;

    private String chassisNumber;

    @NotBlank
    private String vehicleModel;

    private String variant;

    private String color;

    @NotNull
    private FuelType fuelType;

    @PositiveOrZero
    private int kmReading;

    @NotNull
    @PastOrPresent
    private LocalDate purchaseDate;

    private String sellingLocation;

    private String sellingDealer;
    
    private String transmisionType;
    
    private String vehicleMake;
    
    private LocalDate vehicleMakeYear;

}
