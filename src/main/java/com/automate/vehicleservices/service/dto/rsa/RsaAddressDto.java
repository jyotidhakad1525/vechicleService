package com.automate.vehicleservices.service.dto.rsa;

import com.automate.vehicleservices.entity.RSAAddress;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
public class RsaAddressDto implements Serializable {
    private static final long serialVersionUID = 5118092635194542012L;

    private int id;

    private String address;

    private String area;

    private String landmark;

    private float latitude;

    private float longitude;

    private String pin;

    public RsaAddressDto(RSAAddress rsaAddress) {
        this.id = rsaAddress.getId();
        this.address = rsaAddress.getAddress();
        this.area = rsaAddress.getArea();
        this.landmark = rsaAddress.getLandmark();
        this.latitude = rsaAddress.getLatitude();
        this.longitude = rsaAddress.getLongitude();
        this.pin = rsaAddress.getPin();
    }
}
