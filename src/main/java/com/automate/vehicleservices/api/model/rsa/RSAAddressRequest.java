package com.automate.vehicleservices.api.model.rsa;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RSAAddressRequest {
    private String address;

    private String area;

    private String landmark;

    private float latitude;

    private float longitude;

    private String pin;
}
