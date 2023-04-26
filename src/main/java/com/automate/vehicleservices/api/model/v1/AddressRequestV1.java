package com.automate.vehicleservices.api.model.v1;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Chandrashekar V
 */
@Setter
@Getter
@Builder
public class AddressRequestV1 {

    private Integer id;

    @NotNull
    private String pin;

    private String houseNo;

    private String street;

    private String villageOrTown;

    private String city;

    private String mandalOrTahasil;

    private String district;

    private String state;

    private Boolean isUrban;
}
