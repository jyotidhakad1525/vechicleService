package com.automate.vehicleservices.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Chandrashekar V
 */
@Setter
@Getter
public class AddressRequest {

    @NotNull
    private String address;

    private String pin;

    private String state;

    private String city;
    
    private String district;
    
    private String area;
    
    private float longitude;

    private float latitude;

    @NotNull
    private AddressLabel label;

    private String addressLabelIfOther;


}
