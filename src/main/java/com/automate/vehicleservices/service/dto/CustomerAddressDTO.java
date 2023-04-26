package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.api.model.AddressLabel;
import com.automate.vehicleservices.entity.CustomerAddress;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor
@Data
public class CustomerAddressDTO {
    private String address;
    private AddressLabel addressLabel;
    private String addressLabelOtherName;
    private String city;
    private String district;
    private String area;
    private boolean isDefault;
    private String pin;
    private String state;
    private float longitude;
    private float latitude;
    private int id;

    public CustomerAddressDTO(CustomerAddress customerAddress) {
        this.address = customerAddress.getAddress();
        this.addressLabel = customerAddress.getAddressLabel();
        this.addressLabelOtherName = customerAddress.getAddressLabelOtherName();
        this.city = customerAddress.getCity();
        this.district=customerAddress.getDistrict();
        this.area=customerAddress.getArea();
        this.isDefault = Optional.ofNullable(customerAddress.getIsDefault()).orElse(Boolean.FALSE);
        this.pin = customerAddress.getPin();
        this.state = customerAddress.getState();
        this.longitude = Optional.ofNullable(customerAddress.getLongitude()).orElse(0f);
        this.latitude = Optional.of(customerAddress.getLatitude()).orElse(0f);
        this.id = customerAddress.getId();
    }
}
