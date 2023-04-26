package com.automate.vehicleservices.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


/**
 * The persistent class for the vehicle_service_history database table.
 */
@Getter
@Setter
@Entity
@Table(name = "vehicle_service_history")
@NamedQuery(name = "VehicleServiceHistory.findAll", query = "SELECT v FROM VehicleServiceHistory v")
public class VehicleServiceHistory extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private String id;

    @Column(name = "KM_READING")
    private Integer kmReading;

    @Lob
    private String remarks;

    @Column(name = "SERVICE_AMOUNT")
    private Float serviceAmount;

    @Column(name = "SERVICE_CENTER", length = 255)
    private String serviceCenter;

    @Column(name = "SERVICE_LOCATION", length = 255)
    private String serviceLocation;

    @Column(name = "SERVICE_DATE")
    private LocalDate serviceDate;

    @Column(name = "SERVICE_MANAGER", length = 255)
    private String serviceManager;
    
    @Column(name = "DEALER_NAME", length = 45)
    private String dealerName;
    
    @Column(name = "DEALER_LOCATION", length = 45)
    private String dealerLocation;
    
    @Column(name = "LAST_SERVICE_FEEDBACK", length = 45)
    private String lastServiceFeedback;
    
    @Column(name = "REASON_FOR_COMPLAINT", length = 45)
    private String reasonForComplaint;
    
    @Column(name = "COMPLAINT_STATUS", length = 45)
    private String complaintStatus;

    //bi-directional many-to-one association to MdServiceType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_TYPE")
    private MdServiceType mdServiceType;

    //bi-directional many-to-one association to MdTenant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID")
    private MdTenant mdTenant;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH,
            CascadeType.DETACH})
    @JoinColumn(name = "VEHICLE_ID")
    @JsonBackReference
    private ServiceVehicle serviceVehicle;

    @ManyToOne
    @JoinColumn(name = "ro_id")
    private RoDataDetails roData;

    @Override
    public String toString() {
        return "VehicleServiceHistory{" +
                "id='" + id + '\'' +
                ", kmReading=" + kmReading +
                ", remarks='" + remarks + '\'' +
                ", serviceAmount=" + serviceAmount +
                ", serviceCenter='" + serviceCenter + '\'' +
                ", serviceDate=" + serviceDate +
                ", serviceManager='" + serviceManager + '\'' +
                ", mdServiceType=" + (null != mdServiceType ? mdServiceType.getId() : "NULL") +
                ", mdTenant=" + (null != mdTenant ? mdTenant.getTenantIdentifier() : "NULL") +
                ", serviceVehicle=" + serviceVehicle +
                ", dealerName=" + dealerName +
                ", dealerLocation=" + dealerLocation +
                ", lastServiceFeedback=" + lastServiceFeedback +
                ", reasonForComplaint=" + reasonForComplaint +
                ", complaintStatus=" + complaintStatus +
                
                '}';
    }
}