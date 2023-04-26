package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.RSA;
import com.automate.vehicleservices.entity.RSAAddress;

public final class RsaAddressBuilder {
    private int id;
    private String address;
    private String area;
    private String landmark;
    private float latitude;
    private float longitude;
    private String pin;
    private RSA rSA;

    private RsaAddressBuilder() {
    }

    public static RsaAddressBuilder aRsaAddress() {
        return new RsaAddressBuilder();
    }

    public RsaAddressBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public RsaAddressBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public RsaAddressBuilder withArea(String area) {
        this.area = area;
        return this;
    }

    public RsaAddressBuilder withLandmark(String landmark) {
        this.landmark = landmark;
        return this;
    }

    public RsaAddressBuilder withLatitude(float latitude) {
        this.latitude = latitude;
        return this;
    }

    public RsaAddressBuilder withLongitude(float longitude) {
        this.longitude = longitude;
        return this;
    }

    public RsaAddressBuilder withPin(String pin) {
        this.pin = pin;
        return this;
    }

    public RsaAddressBuilder withRSA(RSA rSA) {
        this.rSA = rSA;
        return this;
    }

    public RSAAddress build() {
        RSAAddress rsaAddress = new RSAAddress();
        rsaAddress.setId(id);
        rsaAddress.setAddress(address);
        rsaAddress.setArea(area);
        rsaAddress.setLandmark(landmark);
        rsaAddress.setLatitude(latitude);
        rsaAddress.setLongitude(longitude);
        rsaAddress.setPin(pin);
        rsaAddress.setRSA(rSA);
        return rsaAddress;
    }
}
