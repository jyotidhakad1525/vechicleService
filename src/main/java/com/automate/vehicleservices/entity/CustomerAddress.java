package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.api.model.AddressLabel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the customer_address database table.
 */
@Entity
@Table(name = "customer_address")
@NamedQuery(name = "CustomerAddress.findAll", query = "SELECT c FROM CustomerAddress c")
@Getter
@Setter
public class CustomerAddress implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Deprecated
    @Column(length = 1000)
    private String address;

    @Deprecated
    @Column(name = "ADDRESS_LABEL", length = 1)
    @Enumerated(EnumType.STRING)
    private AddressLabel addressLabel;

    @Deprecated
    @Column(name = "ADDRESS_LABEL_OTHER_NAME", length = 255)
    private String addressLabelOtherName;

    private String houseNo;

    private String street;

    private String villageOrTown;

    private String mandalOrTahasil;

    private Boolean isUrban;

    @Column(length = 50)
    private String city;

    @Deprecated
    @Column(name = "IS_DEFAULT", columnDefinition = "TINYINT")
    private Boolean isDefault;

    @Column(length = 10)
    private String pin;

    @Column(length = 50)
    private String state;

    @Column(length = 45)
    private String district;

    @Deprecated
    @Column(length = 45)
    private String area;

    @Deprecated
    @Column(name = "geo_location_longitude")
    private Float longitude;

    @Deprecated
    @Column(name = "geo_location_latitude")
    private Float latitude;

    //bi-directional many-to-one association to Customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    @JsonBackReference
    private Customer customer;

}