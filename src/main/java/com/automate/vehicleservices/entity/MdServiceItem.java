package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.repository.dtoprojection.ServiceItemByGroup;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the md_service_item database table.
 */
@SqlResultSetMapping(name = "serviceItemsByServiceGroup", classes = {
        @ConstructorResult(
                targetClass = ServiceItemByGroup.class,
                columns = {
                        @ColumnResult(name = "itemName  ", type = String.class),
                        @ColumnResult(name = "serviceItemId", type = Integer.class),
                        @ColumnResult(name = "serviceTypeId", type = Integer.class)
                }
        )
})
@Entity
@Table(name = "md_service_item")
@NamedQuery(name = "MdServiceItem.findAll", query = "SELECT m FROM MdServiceItem m")
@Getter
@Setter
public class MdServiceItem extends Auditable implements Serializable {

    private static final long serialVersionUID = 8702263979551684865L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(length = 255)
    private String description;

    @Column(name = "IS_ACTIVE", columnDefinition = "TINYINT")
    private Boolean isActive;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "SHORT_CODE", length = 20)
    private String shortCode;

    //bi-directional many-to-one association to MdTenant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID", nullable = false)
    @JsonBackReference
    private MdTenant mdTenant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MdServiceItem that = (MdServiceItem) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}