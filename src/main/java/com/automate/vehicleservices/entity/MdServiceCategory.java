package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.entity.enums.ServiceGroup;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * The persistent class for the md_service_category database table.
 */
@Entity
@Table(name = "md_service_category")
@NamedQuery(name = "MdServiceCategory.findAll", query = "SELECT m FROM MdServiceCategory m")
@Getter
@Setter
public class MdServiceCategory extends Auditable implements Serializable {

    private static final long serialVersionUID = -5440284285782449216L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "CATEGORY_NAME", nullable = false, length = 45)
    private String categoryName;

    @Column(name = "IS_ACTIVE", columnDefinition = "TINYINT")
    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "SERVICE_GROUP", nullable = false)
    private ServiceGroup serviceGroup;

    //bi-directional many-to-one association to MdServiceType
    @OneToMany(mappedBy = "mdServiceCategory", cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JsonManagedReference
    private List<MdServiceType> mdServiceTypes = new ArrayList<>();

    //bi-directional many-to-one association to MdTenant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID", nullable = false)
    @JsonBackReference
    private MdTenant mdTenant;

    public MdServiceType addMdServiceType(MdServiceType mdServiceType) {
        getMdServiceTypes().add(mdServiceType);
        mdServiceType.setMdServiceCategory(this);

        return mdServiceType;
    }

    public MdServiceType removeMdServiceType(MdServiceType mdServiceType) {
        getMdServiceTypes().remove(mdServiceType);
        mdServiceType.setMdServiceCategory(null);

        return mdServiceType;
    }

    public List<MdServiceType> getMdServiceTypes() {
        if (Objects.isNull(mdServiceTypes))
            mdServiceTypes = new ArrayList<>();
        return mdServiceTypes;
    }
}