package com.automate.vehicleservices.entity;


import com.automate.vehicleservices.entity.enums.ActiveInActiveStatus;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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

@Data
@Entity
@Table(name = "service_logic_details")
public class ServiceLogicConfigurationDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "service_type")
    private MdServiceCategory serviceType;

    @ManyToOne
    @JoinColumn(name = "sub_service_type")
    private MdServiceType subServiceType;

    @Column(name = "start_day")
    private Integer startDay;

    @Column(name = "end_day")
    private Integer endDay;

    @Column(name = "km_start")
    private Integer kmStart;

    @Column(name = "km_end")
    private Integer kmEnd;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ActiveInActiveStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    private MdOrganization organization;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_datetime")
    private Date createdDatetime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_datetime")
    private Date updatedDatetime;
}
