package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.entity.enums.CommunicationModeEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Chandrashekar V
 */
@Entity
@Table(name = "md_communication_mode")
@Getter
@Setter
public class CommunicationMode extends Auditable implements Serializable {

    private static final long serialVersionUID = 6418623609016444483L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Enumerated(EnumType.STRING)
    private CommunicationModeEnum type;

    //bi-directional many-to-one association to MdTenant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID", nullable = false)
    @JsonBackReference
    private MdTenant mdTenant;

    @Column(name = "IS_ACTIVE", columnDefinition = "TINYINT")
    private Boolean active;

}
