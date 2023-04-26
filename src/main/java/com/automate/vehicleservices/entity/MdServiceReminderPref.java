package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.entity.enums.Expression;
import com.automate.vehicleservices.entity.enums.TimeFrame;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the vehicle_km_tracker database table.
 */
@Getter
@Setter
@Entity
@Table(name = "md_service_reminder_prefs")
@NamedQuery(name = "MdServiceReminderPref.findAll", query = "SELECT v FROM MdServiceReminderPref v")
public class MdServiceReminderPref extends Auditable implements Serializable {
    private static final long serialVersionUID = 7836546374696730917L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "DURATION_VALUE", nullable = false)
    private int durationValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "EXPRESSION")
    private Expression expression;

    @Enumerated(EnumType.STRING)
    @Column(name = "DURATION_TIME_FRAME")
    private TimeFrame timeFrame;

    @Column(name = "IS_ACTIVE", nullable = false, columnDefinition = "TINYINT")
    private Boolean isActive;

    //uni-directional many-to-one association to ServiceVehicle
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_TYPE", nullable = false)
    private MdServiceType mdServiceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODE_OF_COMMUNICATION", nullable = false)
    private CommunicationMode communicationMode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID", nullable = false)
    private MdTenant mdTenant;

}