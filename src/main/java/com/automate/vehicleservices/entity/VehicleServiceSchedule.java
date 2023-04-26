package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.entity.enums.ScheduleStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


/**
 * The persistent class for the vehicle_service_schedule database table. As soon as there is a new entry in vehicle km
 * tracker, we update the vehicle service schedule. The same is used by service reminder to schedule reminders based on
 * reminder preferences.
 */
@Setter
@Getter
@Entity
@Table(name = "vehicle_service_schedule")
@NamedQuery(name = "VehicleServiceSchedule.findAll", query = "SELECT v FROM VehicleServiceSchedule v")
public class VehicleServiceSchedule extends Auditable implements Serializable {

    private static final long serialVersionUID = 7430917272559538918L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "LAST_MODIFIED_REASON", length = 255)
    private String lastModifiedReason;

    @Column(name = "SERVICE_DUE_DATE_PREFERRED")
    private LocalDate serviceDueDatePreferred;

    @Column(name = "SERVICE_DUE_DATE_PER_SCHEDULE")
    private LocalDate serviceDueDatePerSchedule;

    @Column(name = "SERVICE_DUE_DATE_RECOMMENDED")
    private LocalDate serviceDueDateRecommended;

    @Column(name = "LAST_SERVICE_DATE")
    private LocalDate lastServiceDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VEHICLE_ID", nullable = false)
    private ServiceVehicle serviceVehicle;

    //uni-directional many-to-one association to MdServiceType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_TYPE", nullable = true)
    private MdServiceType mdServiceType;

    //bi-directional many-to-one association to MdTenant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID")
    @JsonBackReference
    private MdTenant mdTenant;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    @Column(name = "KM_BETWEEN_LAST_TWO_SERVICES")
    private int kmBetweenLastTwoServices;

    @Column(name = "DIFFERENCE_IN_DAYS_BETWEEN_LAST_TWO_SERVICES")
    private int differenceInDaysBetweenLastTwoServices;

    @Column(name = "AVERAGE_KM")
    private Float averageKM;

    @Column(name = "SERVICE_CATEGORY")
    private String serviceCategory;

    @Override
    public String toString() {
        return "VehicleServiceSchedule{" +
                "id=" + id +
                ", lastModifiedReason='" + lastModifiedReason + '\'' +
                ", serviceDueDatePreferred=" + serviceDueDatePreferred +
                ", serviceDueDatePerSchedule=" + serviceDueDatePerSchedule +
                ", serviceDueDateRecommended=" + serviceDueDateRecommended +
                ", lastServiceDate=" + lastServiceDate +
                ", serviceVehicle=" + serviceVehicle +
                ", mdServiceType=" + (null != mdServiceType ? mdServiceType.getServiceName() : "NULL") +
                ", mdServiceType=" + (null != mdServiceType ? mdServiceType.getId() : -1) +
                ", mdTenant=" + mdTenant +
                ", status=" + status +
                ", kmBetweenLastTwoServices=" + kmBetweenLastTwoServices +
                ", differenceInDaysBetweenLastTwoServices=" + differenceInDaysBetweenLastTwoServices +
                ", averageKM=" + averageKM +
                ", serviceCategory=" + serviceCategory +
                '}';
    }
}