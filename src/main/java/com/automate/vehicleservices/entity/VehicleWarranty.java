package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.entity.enums.WarrantyStatus;
import com.automate.vehicleservices.entity.enums.WarrantyType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "vehicle_warranty")
public class VehicleWarranty extends Auditable implements Serializable {
    private static final long serialVersionUID = -5834875005162480271L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "EXPIRY_DATE")
    private LocalDate expiryDate;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private WarrantyStatus status;

    @Column(name = "WARRANTY_AMOUNT_PAID")
    private Double amountPaid;
    
    @Column(name = "OEM_PERIOD")
    private String oemPeriod;
    
    @Column(name = "EW_NAME")
    private String ewName;
    
    @Column(name = "AMC_NAME")
    private String amc_name;

    @Deprecated
    @Column(name = "FASTAG_STATUS")
    private String fastagStatus;

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "WARRANTY_TYPE")
    @Enumerated(EnumType.STRING)
    private WarrantyType warrantyTpe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID")
    private MdTenant mdTenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VEHICLE_ID")
    @JsonBackReference
    private ServiceVehicle serviceVehicle;

}
