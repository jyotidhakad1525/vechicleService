package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.entity.enums.FuelType;
import com.automate.vehicleservices.entity.enums.VehicleStatus;
import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * The persistent class for the service_vehicle database table.
 */
@Getter
@Setter
@Entity
@Table(name = "service_vehicle")
@NamedQuery(name = "ServiceVehicle.findAll", query = "SELECT s FROM ServiceVehicle s")
//@EntityListeners(ServiceVehicleEntityListener.class)
@SqlResultSetMapping(name = "UnscheduledServiceVehicles", classes = @ConstructorResult(
        targetClass = ServiceVehicleDTO.class,
        columns = {
                @ColumnResult(name = "regNumber", type = String.class),
                @ColumnResult(name = "tenantId", type = String.class),
                @ColumnResult(name = "purchaseDate", type = LocalDate.class),
        }
))
public class ServiceVehicle extends Auditable implements Serializable {
    private static final long serialVersionUID = -2134364164751705556L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Deprecated
    @Column(name = "ADDITIONAL_DETAILS", length = 255)
    private String additionalDetails;

    @Column(name = "CHASSIS_NUMBER", length = 50)
    private String chassisNumber;

    @Column(length = 255)
    private String model;

    @Column(name = "PURCHASE_DATE")
    private LocalDate purchaseDate;

    @Column(name = "REG_NUMBER", unique = true, nullable = false, length = 50)
    @NaturalId
    private String regNumber;

    @Column(name = "ENGINE_NUMBER")
    private String engineNumber;

    @Column(name = "VARIANT")
    private String variant;

    @Column(name = "COLOR")
    private String color;

    @Column(length = 255)
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Column(length = 255, nullable = true)
    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    @Column(length = 255)
    private String vin;

    @Column(name = "SELLING_LOCATION")
    private String sellingLocation;

    @Column(name = "TRANSMITION_TYPE",length = 45)
    private String transmisionType;
    
    @Column(name = "VEHICLE_MAKE",length = 45)
    private String vehicleMake;

    @Deprecated
    @Column(name = "VEHICLE_MAKE_YEAR")
    private LocalDate vehicleMakeYear;
    
    @Column(name = "SELLING_DEALER")
    private String sellingDealer;
    
    @Column(name = "MAKING_MONTH",length = 45)
    private String makingMonth;

    private Integer currentKmReading;

    private Integer makingYear;

    private Boolean isFastag;

    @ManyToOne
    @JoinColumn(name = "on_going_service")
    private MdServiceType serviceType;

    @Column(name = "on_going_service_due_date")
    private LocalDate dueDate;

    //bi-directional many-to-one association to Customer
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH,
            CascadeType.DETACH})
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    @JsonBackReference
    private Customer customer;

    @OneToMany(mappedBy = "serviceVehicle", cascade = CascadeType.ALL)
    @JsonManagedReference
    @OrderBy("serviceDate DESC")
    private List<VehicleServiceHistory> vehicleServiceHistories = new ArrayList<>();

    @OneToMany(mappedBy = "vehicle")
    @JsonManagedReference
    private List<LeadAllocationDetails> leadAllocationDetails = new ArrayList<>();

    @OneToMany(mappedBy = "serviceVehicle")
    @JsonManagedReference
    private List<ServiceAppointment> appointments = new ArrayList<>();

    //bi-directional many-to-one association to VehicleKmTracker
    @OneToMany(mappedBy = "serviceVehicle", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<VehicleKmTracker> vehicleKmTrackers = new ArrayList<>();

    //bi-directional many-to-one association to VehicleKmTracker
    @OneToMany(mappedBy = "serviceVehicle", cascade = CascadeType.ALL)
    @JsonManagedReference
//    @Where(clause = "INSURANCE_END_DATE > CURRENT_TIMESTAMP()")
    private List<VehicleInsurance> vehicleInsurances = new ArrayList<>();

    @OneToMany(mappedBy = "serviceVehicle", cascade = CascadeType.ALL)
    @JsonManagedReference
//    @Where(clause = "EXPIRY_DATE > CURRENT_TIMESTAMP()")
    private List<VehicleWarranty> vehicleWarranties = new ArrayList<>();

    //uni-directional many-to-one association to MdTenant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID")
    private MdTenant mdTenant;


    public VehicleKmTracker addVehicleKmTracker(VehicleKmTracker vehicleKmTracker) {
        getVehicleKmTrackers().add(vehicleKmTracker);
        vehicleKmTracker.setServiceVehicle(this);

        return vehicleKmTracker;
    }

    public VehicleKmTracker removeVehicleKmTracker(VehicleKmTracker vehicleKmTracker) {
        getVehicleKmTrackers().remove(vehicleKmTracker);
        vehicleKmTracker.setServiceVehicle(null);

        return vehicleKmTracker;
    }

    public List<VehicleKmTracker> getVehicleKmTrackers() {
        if (Objects.isNull(vehicleKmTrackers))
            vehicleKmTrackers = new ArrayList<>();
        return vehicleKmTrackers;
    }


    public VehicleInsurance addVehicleInsurance(VehicleInsurance vehicleInsurance) {
        getVehicleInsurances().add(vehicleInsurance);
        vehicleInsurance.setServiceVehicle(this);

        return vehicleInsurance;
    }

    public VehicleInsurance removeVehicleInsurance(VehicleInsurance vehicleInsurance) {
        getVehicleInsurances().remove(vehicleInsurance);
        vehicleInsurance.setServiceVehicle(null);

        return vehicleInsurance;
    }


    public VehicleWarranty addVehicleKmTracker(VehicleWarranty vehicleWarranty) {
        getVehicleWarranties().add(vehicleWarranty);
        vehicleWarranty.setServiceVehicle(this);

        return vehicleWarranty;
    }

    public VehicleWarranty removeVehicleWarranty(VehicleWarranty vehicleWarranty) {
        getVehicleWarranties().remove(vehicleWarranty);
        vehicleWarranty.setServiceVehicle(null);

        return vehicleWarranty;
    }

    public List<VehicleInsurance> getVehicleInsurances() {
        if (Objects.isNull(this.vehicleInsurances))
            this.vehicleInsurances = new ArrayList<>();
        return vehicleInsurances;
    }

    public List<VehicleWarranty> getVehicleWarranties() {
        if (Objects.isNull(this.vehicleWarranties))
            this.vehicleWarranties = new ArrayList<>();
        return vehicleWarranties;
    }
}
