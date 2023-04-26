package com.automate.vehicleservices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "doorstep_service")
@NamedQuery(name = "DoorstepService.findAll", query = "SELECT d FROM DoorstepService d")
@Getter
@Setter
public class DoorstepService implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String address;

    @Column(name = "allotment_id")
    private int allotmentId;

    @Column(name = "branch_id")
    private Integer branchId;

    @Column(name = "customer_id")
    private String customerId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "doorstep_service_datetime")
    private Date doorstepServiceDatetime;

    @Column(name = "driver_id")
    private Integer driverId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Date endTime;

    private String latitude;

    private String longitude;

    @Column(name = "org_id")
    private Integer orgId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time")
    private Date startTime;

}