package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.repository.dtoprojection.RateCardItem;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the md_service_rate_card database table. String shortCode, String maintenanceItemName,
 * double rate, String serviceName, String maintenanceType)
 */

@SqlResultSetMapping(name = "RateCardByTenantVehicleVariant", classes = {
        @ConstructorResult(
                targetClass = RateCardItem.class,
                columns = {
                        @ColumnResult(name = "shortCode", type = String.class),
                        @ColumnResult(name = "maintenanceItemName", type = String.class),
                        @ColumnResult(name = "rate", type = Double.class),
                        @ColumnResult(name = "serviceName", type = String.class),
                        @ColumnResult(name = "serviceTypeId", type = Integer.class),
                        @ColumnResult(name = "maintenanceType", type = String.class),
                        @ColumnResult(name = "category", type = String.class),
                        @ColumnResult(name = "categoryId", type = Integer.class)
                }
        )
})
@Table(name = "md_service_rate_card")
@NamedQuery(name = "MdServiceRateCard.findAll", query = "SELECT m FROM MdServiceRateCard m")
@Getter
@Setter
@Entity
public class MdServiceRateCard implements Serializable {
    private static final long serialVersionUID = -6714628823622889648L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "APPLICABLE_FOR_DIESEL", columnDefinition = "TINYINT")
    private Boolean applicableForDiesel;

    @Column(name = "APPLICABLE_FOR_ELECTRIC", columnDefinition = "TINYINT")
    private Boolean applicableForElectric;

    @Column(name = "APPLICABLE_FOR_HYBRID", columnDefinition = "TINYINT")
    private Boolean applicableForHybrid;

    @Column(name = "APPLICABLE_FOR_PETROL", columnDefinition = "TINYINT")
    private Boolean applicableForPetrol;

    private Double rate;

    @Column(name = "VEHICLE_MODEL", length = 100)
    private String vehicleModel;

    //uni-directional many-to-one association to MdMaintenanceType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAINTENANCE_TYPE_ID")
    private MdMaintenanceType mdMaintenanceType;

    //uni-directional many-to-one association to MdServiceItem
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_ITEM_ID")
    private MdServiceItem mdServiceItem;

    //uni-directional many-to-one association to MdServiceType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_TYPE_ID", nullable = false)
    private MdServiceType mdServiceType;

    //uni-directional many-to-one association to MdServiceTypeCriteria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_CRITERIA_ID")
    private MdServiceTypeCriteria mdServiceTypeCriteria;

    //bi-directional many-to-one association to MdTenant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID", nullable = false)
    @JsonBackReference
    private MdTenant mdTenant;

}