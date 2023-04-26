package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.api.model.AddressLabel;
import com.automate.vehicleservices.entity.Customer;
import com.automate.vehicleservices.entity.CustomerAddress;

public final class CustomerAddressBuilder {
    private Integer id;
    private String address;
    private AddressLabel addressLabel;
    private String addressLabelOtherName;
    private String city;
    private String area;
    private String district;
    private String pin;
    private String state;
    private float longitude;
    private float latitude;
    private Customer customer;

    private String houseNumber;

    private String street;

    private String villageOrTown;

    private String mandalOrTahasil;

    private Boolean isUrban;

    private CustomerAddressBuilder() {
    }

    public static CustomerAddressBuilder aCustomerAddress() {
        return new CustomerAddressBuilder();
    }

    public CustomerAddressBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public CustomerAddressBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public CustomerAddressBuilder withHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
        return this;
    }

    public CustomerAddressBuilder withStreet(String street) {
        this.street = street;
        return this;
    }

    public CustomerAddressBuilder withVillageOrTown(String villageOrTown) {
        this.villageOrTown = villageOrTown;
        return this;
    }

    public CustomerAddressBuilder withMandalOrTahasil(String mandalOrTahasil) {
        this.mandalOrTahasil = mandalOrTahasil;
        return this;
    }

    public CustomerAddressBuilder withIsUrban(Boolean isUrban) {
        this.isUrban = isUrban;
        return this;
    }

    public CustomerAddressBuilder withAddressLabel(AddressLabel addressLabel) {
        this.addressLabel = addressLabel;
        return this;
    }

    public CustomerAddressBuilder withAddressLabelOtherName(String addressLabelOtherName) {
        this.addressLabelOtherName = addressLabelOtherName;
        return this;
    }

    public CustomerAddressBuilder withCity(String city) {
        this.city = city;
        return this;
    }
    public CustomerAddressBuilder withArea(String area) {
        this.area = area;
        return this;
    }
    public CustomerAddressBuilder withDistrict(String district) {
        this.district = district;
        return this;
    }

    public CustomerAddressBuilder withPin(String pin) {
        this.pin = pin;
        return this;
    }

    public CustomerAddressBuilder withState(String state) {
        this.state = state;
        return this;
    }

    public CustomerAddressBuilder withLongitude(float longitude) {
        this.longitude = longitude;
        return this;
    }

    public CustomerAddressBuilder withLatitude(float latitude) {
        this.latitude = latitude;
        return this;
    }

    public CustomerAddressBuilder withCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public CustomerAddress build() {
        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setId(id);
        customerAddress.setAddress(address);
        customerAddress.setAddressLabel(addressLabel);
        customerAddress.setAddressLabelOtherName(addressLabelOtherName);
        customerAddress.setCity(city);
        customerAddress.setDistrict(district);
        customerAddress.setArea(area);
        customerAddress.setPin(pin);
        customerAddress.setState(state);
        customerAddress.setLongitude(longitude);
        customerAddress.setLatitude(latitude);
        customerAddress.setCustomer(customer);
        customerAddress.setHouseNo(houseNumber);
        customerAddress.setStreet(street);
        customerAddress.setVillageOrTown(villageOrTown);
        customerAddress.setMandalOrTahasil(mandalOrTahasil);
        customerAddress.setIsUrban(isUrban);
        return customerAddress;
    }
}
