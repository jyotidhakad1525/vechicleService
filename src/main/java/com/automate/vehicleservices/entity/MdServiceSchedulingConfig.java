package com.automate.vehicleservices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "md_service_scheduling_config")
@NamedQuery(name = "MdServiceSchedulingConfig.findAll", query = "SELECT s FROM MdServiceSchedulingConfig s")
public class MdServiceSchedulingConfig extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIRST_FREE_SERVICE_TYPE")
    private MdServiceType firstFreeServiceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PMC_SERVICE_TYPE")
    private MdServiceType pmcServiceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PMS_SERVICE_CATEGORY")
    private MdServiceCategory pmsServiceCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PMC_SERVICE_CATEGORY")
    private MdServiceCategory pmcServiceCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FREE_SERVICE_CATEGORY")
    private MdServiceCategory freeServiceCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID")
    private MdTenant mdTenant;

    @Column(name = "PMS_INTERVAL_MONTHS")
    private int pmsIntervalMonths;

    @Column(name = "PMC_INTERVAL_MONTHS")
    private int pmcIntervalMonths;

    @Column(name = "PMS_INTERVAL_KM")
    private int pmsIntervalKm;

    @Column(name = "PMC_INTERVAL_KM")
    private int pmcIntervalKm;

    @Column(name = "REMINDERS_ENABLED", columnDefinition = "TINYINT")
    private Boolean remindersEnabled;

    @JoinColumn(name = "FIRST_FREE_SERVICE_DUE_DAYS_FROM_PURCHASE_DATE")
    private int firstFreeServiceDueDaysFromPurchaseDate;

}
