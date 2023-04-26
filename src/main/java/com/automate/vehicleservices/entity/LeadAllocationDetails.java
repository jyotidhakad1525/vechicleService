package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "lead_allocation_details")
public class LeadAllocationDetails implements Serializable {
    private static final long serialVersionUID = -2134364164751705556L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "CRE_ID", nullable = false)
    private Employee creDetail;

    @Column(name = "CRE_LOCATION")
    private String creLocation;

    @Column(name = "CRE_SERVICE_CENTER_CODE")
    private String serviceCenterCode;

    @ManyToOne
    @JoinColumn(name = "VEHICLE_ID", nullable = false)
    @JsonBackReference
    private ServiceVehicle vehicle;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private ActiveInActiveStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATETIME")
    private Date updatedDatetime;

}
