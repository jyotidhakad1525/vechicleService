package com.automate.vehicleservices.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * The persistent class for the md_organization database table.
 *
 * @author Chandrashekar V
 */
@Entity
@Table(name = "md_organization")
@NamedQuery(name = "MdOrganization.findAll", query = "SELECT m FROM MdOrganization m")
@Getter
@Setter
public class MdOrganization extends Auditable implements Serializable {
    private static final long serialVersionUID = 4074191071679019131L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "MASTER_IDENTIFIER")
    private Integer masterIdentifier;

    @Column(length = 1000)
    private String description;

    @Column(name = "IS_ACTIVE")
    private byte isActive;

    @Column(name = "ORG_IDENTIFIER", unique = true, nullable = false, length = 50)
    @NaturalId
    private String orgIdentifier;

    @Column(name = "ORG_NAME", nullable = false, length = 255)
    private String orgName;

    @Column(name = "ORG_SHORT_CODE", length = 20)
    private String orgShortCode;

    //bi-directional many-to-one association to MdTenant
    @OneToMany(mappedBy = "mdOrganization")
    @JsonManagedReference
    private List<MdTenant> mdTenants = new ArrayList<>();

    public MdTenant addMdTenant(MdTenant mdTenant) {
        getMdTenants().add(mdTenant);
        mdTenant.setMdOrganization(this);

        return mdTenant;
    }

    public MdTenant removeMdTenant(MdTenant mdTenant) {
        getMdTenants().remove(mdTenant);
        mdTenant.setMdOrganization(null);

        return mdTenant;
    }

    public List<MdTenant> getMdTenants() {
        if (Objects.isNull(mdTenants))
            mdTenants = new ArrayList<>();
        return mdTenants;
    }
}