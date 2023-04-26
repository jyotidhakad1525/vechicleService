package com.automate.vehicleservices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the md_rule_constraint database table.
 *
 * @author Chandrashekar V
 */
@Setter
@Getter
@Entity
@Table(name = "md_rule_constraint")
@NamedQuery(name = "MdRuleConstraint.findAll", query = "SELECT m FROM MdRuleConstraint m")
public class MdRuleConstraint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(length = 255)
    private String data;

    @Column(length = 255)
    private String description;

    @Column(length = 1)
    private String expression;

    //bi-directional many-to-one association to MdTenant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID", nullable = false)
    private MdTenant mdTenant;

    //uni-directional many-to-one association to MdRuleConstraintField
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FIELD_ID")
    private MdRuleConstraintField mdRuleConstraintField;

}