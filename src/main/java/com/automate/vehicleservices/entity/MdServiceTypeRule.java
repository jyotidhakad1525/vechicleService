package com.automate.vehicleservices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


/**
 * The persistent class for the md_service_type_rule database table.
 */
@Entity
@Table(name = "md_service_type_rule")
@NamedQuery(name = "MdServiceTypeRule.findAll", query = "SELECT m FROM MdServiceTypeRule m")
@Getter
@Setter
public class MdServiceTypeRule implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(length = 100)
    private String description;

    @Column(name = "RULE_EXPRESSION", length = 1000, nullable = false)
    private String ruleExpression;

    //bi-directional many-to-one association to MdTenant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID", nullable = false)
    private MdTenant mdTenant;

    //uni-directional many-to-one association to MdServiceType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_TYPE_ID", nullable = false)
    private MdServiceType mdServiceType;

}