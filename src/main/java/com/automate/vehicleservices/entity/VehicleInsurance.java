package com.automate.vehicleservices.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


/**
 * The persistent class for the service_vehicle database table.
 */
@Getter
@Setter
@Entity
@Table(name = "vehicle_insurance")
public class VehicleInsurance extends Auditable implements Serializable {
    private static final long serialVersionUID = -2134364164751705556L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "INSURANCE_ID", length = 255)
    private String insuranceIdentifier;

    @Column(name = "PROVIDER", length = 255)
    private String provider;

    @Column(name = "INSURED_AMOUNT")
    private Double insuranceAmount;

    @Column(name = "INSURANCE_START_DATE")
    private LocalDate startDate;

    @Column(name = "INSURANCE_END_DATE")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "VEHICLE_ID")
    @JsonBackReference
    private ServiceVehicle serviceVehicle;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "vehicle_insurance_document", joinColumns = {@JoinColumn(name = "INSURANCE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "DOCUMENT_ID")})
    private Set<ServiceDocument> serviceDocuments = new HashSet<>();

}
