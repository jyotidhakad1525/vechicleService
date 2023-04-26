package com.automate.vehicleservices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the md_rule_constraint_field database table.
 */
@Entity
@Table(name = "md_rule_constraint_field")
@NamedQuery(name = "MdRuleConstraintField.findAll", query = "SELECT m FROM MdRuleConstraintField m")
@Getter
@Setter
public class MdRuleConstraintField implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "FIELD_DATA_TYPE")
    private String fieldDataType;

    @Column(name = "FIELD_NAME", length = 45)
    private String fieldName;

    //bi-directional many-to-one association to MdTenant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID", nullable = false)
    private MdTenant mdTenant;


}