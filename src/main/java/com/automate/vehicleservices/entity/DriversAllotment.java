package com.automate.vehicleservices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "drivers_allotment", schema = "vehicle-services")
@NamedQuery(name = "DriversAllotment.findAll", query = "SELECT d FROM DriversAllotment d")
@Getter
@Setter
public class DriversAllotment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "org_id")
    private Integer orgId;

    @Column(name = "branch_id")
    private Integer branchId;

    @Column(name = "created_by")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_datetime")
    private Date createdDatetime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "in_datetime")
    private Date inDatetime;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_datetime")
    private Date modifiedDatetime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "out_datetime")
    private Date outDatetime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "planned_end_datetime")
    private Date plannedEndDatetime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "planned_start_datetime")
    private Date plannedStartDatetime;

    // bi-directional many-to-one association to Driver
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    // bi-directional many-to-one association to ServiceAppointment
//	@ManyToOne
    @Column(name = "service_appointment_id")
    private int serviceAppointment;


}