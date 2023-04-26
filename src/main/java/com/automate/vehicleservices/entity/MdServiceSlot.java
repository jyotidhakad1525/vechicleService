package com.automate.vehicleservices.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;


/**
 * The persistent class for the md_service_slots database table.
 */
@Entity
@Table(name = "md_service_slots")
@NamedQuery(name = "MdServiceSlot.findAll", query = "SELECT m FROM MdServiceSlot m")
@Getter
@Setter
public class MdServiceSlot implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    private short availability;

    @Column(name = "SLOT_TIME_FROM", nullable = false)
    private LocalTime slotTimeFrom;

    @Column(name = "SLOT_TIME_TO", nullable = false)
    private LocalTime slotTimeTo;

    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

    @Column(name = "IS_ACTIVE", columnDefinition = "TINYINT")
    private Boolean active;

    //bi-directional many-to-one association to MdTenant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID", nullable = false)
    @JsonBackReference
    private MdTenant mdTenant;
}