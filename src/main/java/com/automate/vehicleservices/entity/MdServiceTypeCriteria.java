package com.automate.vehicleservices.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * The persistent class for the md_service_type_criteria database table.
 */
@Entity
@Table(name = "md_service_type_criteria")
@NamedQuery(name = "MdServiceTypeCriteria.findAll", query = "SELECT m FROM MdServiceTypeCriteria m")
@Getter
@Setter
public class MdServiceTypeCriteria extends Auditable implements Serializable, Comparable<MdServiceTypeCriteria> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(length = 500)
    private String description;

    @Column(name = "DURATION_DAYS_TO")
    private int durationDaysTo;

    @Column(name = "DURATION_DAYS_FROM")
    private int durationDaysFrom;

    @Column(name = "MILEAGE_FROM")
    private int mileageFrom;

    @Column(name = "MILEAGE_TO")
    private int mileageTo;

    //bi-directional many-to-one association to MdTenant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID", nullable = false)
    private MdTenant mdTenant;

    //uni-directional many-to-one association to MdServiceType
    @ManyToOne
    @JoinColumn(name = "SERVICE_TYPE_ID", nullable = false)
    @JsonBackReference
    private MdServiceType mdServiceType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MdServiceTypeCriteria that = (MdServiceTypeCriteria) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(MdServiceTypeCriteria o) {
        if (o == null)
            return 1;
        return this.getDurationDaysFrom() >= o.getDurationDaysFrom() ? 1 : -1;
    }

    @Override
    public String toString() {
        return "MdServiceTypeCriteria{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", durationDaysTo=" + durationDaysTo +
                ", durationDaysFrom=" + durationDaysFrom +
                ", mileageFrom=" + mileageFrom +
                ", mileageTo=" + mileageTo +
                '}';
    }
}