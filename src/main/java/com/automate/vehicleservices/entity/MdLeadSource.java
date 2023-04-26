package com.automate.vehicleservices.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Chandrashekar V
 */
@Entity
@Table(name = "md_lead_source")
@Data
public class MdLeadSource extends Auditable implements Serializable {

    private static final long serialVersionUID = -5166542077256382911L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID", nullable = false)
    private MdTenant mdTenant;

    @Column(name = "IS_ACTIVE", columnDefinition = "TINYINT")
    private Boolean active;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "parent_id")
    private MdLeadSource parentId;


}
