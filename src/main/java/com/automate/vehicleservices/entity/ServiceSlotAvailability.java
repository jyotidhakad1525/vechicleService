package com.automate.vehicleservices.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * The persistent class for the service_slot_availability database table. Holds slots for current date by service type.
 */
@Setter
@Getter
@Entity
@Table(name = "service_slot_availability")
@NamedQuery(name = "ServiceSlotAvailability.findAll", query = "SELECT m FROM ServiceSlotAvailability m")
public class ServiceSlotAvailability implements Serializable {

    private static final long serialVersionUID = 8969430184759391663L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "IS_ACTIVE", columnDefinition = "TINYINT")
    private Boolean isActive;

    @Column(name = "CREATED_BY", length = 255)
    private String createdBy;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "LAST_MODIFIED_BY", length = 255)
    private String lastModifiedBy;

    @Column(name = "LAST_MODIFIED_DATE")
    private LocalDateTime lastModifiedDate;

    @Column(name = "SERVICE_DATE")
    private LocalDate serviceDate;

    //uni-directional many-to-one association to MdServiceSlot
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SLOT_ID")
    private MdServiceSlot mdServiceSlot;

    //bi-directional many-to-one association to MdTenant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID")
    @JsonBackReference
    private MdTenant mdTenant;

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

}