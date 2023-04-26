package com.automate.vehicleservices.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the md_maintenance_type database table.
 */
@Entity
@Table(name = "md_maintenance_type")
@NamedQuery(name = "MdMaintenanceType.findAll", query = "SELECT m FROM MdMaintenanceType m")
@Getter
@Setter
public class MdMaintenanceType extends Auditable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(length = 500)
    private String description;

    @Column(name = "SHORT_CODE", length = 5)
    private String shortCode;

    @Column(nullable = false, length = 255)
    private String type;

    @Column(name = "IS_ACTIVE", columnDefinition = "TINYINT")
    private Boolean active;

    //bi-directional many-to-one association to MdTenant
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID", nullable = false)
    private MdTenant mdTenant;


}