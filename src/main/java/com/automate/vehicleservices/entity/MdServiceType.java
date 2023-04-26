package com.automate.vehicleservices.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


/**
 * The persistent class for the md_service_type database table.
 */
@Entity
@Table(name = "md_service_type")
@NamedQuery(name = "MdServiceType.findAll", query = "SELECT m FROM MdServiceType m")
@Getter
@Setter
public class MdServiceType extends Auditable implements Serializable {
    private static final long serialVersionUID = 3988137093295805105L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(length = 500)
    private String description;

    @Column(name = "IS_ACTIVE", columnDefinition = "TINYINT")
    private Boolean active;

    @Column(name = "SERVICE_CODE", nullable = false, length = 10)
    private String serviceCode;

    @Column(name = "SERVICE_NAME", nullable = false, length = 255)
    private String serviceName;

    @Column(name = "SERVICE_SEQUENCE")
    private Integer serviceSequence;

    //bi-directional many-to-one association to MdTenant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID", nullable = false)
    @JsonBackReference
    private MdTenant mdTenant;

    //bi-directional many-to-one association to MdServiceCategory
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "CATEGORY_ID")
    @JsonBackReference
    private MdServiceCategory mdServiceCategory;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "md_service_type_x_service_item", joinColumns = {@JoinColumn(name = "SERVICE_TYPE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "SERVICE_ITEM_ID")})
    @JsonIgnore
    private Set<MdServiceItem> serviceItems = new HashSet<>();


    //bi-directional many-to-one association to MdServiceTypeCriteria
    @OneToMany(mappedBy = "mdServiceType", fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<MdServiceTypeCriteria> mdServiceTypeCriteria = new ArrayList<>();


    //bi-directional many-to-one association to MdServiceTypeRule
    @OneToMany(mappedBy = "mdServiceType")
    private List<MdServiceTypeRule> mdServiceTypeRules = new ArrayList<>();


    public MdServiceTypeCriteria addMdServiceTypeCriteria(MdServiceTypeCriteria mdServiceTypeCriteria) {
        getMdServiceTypeCriteria().add(mdServiceTypeCriteria);
        mdServiceTypeCriteria.setMdServiceType(this);

        return mdServiceTypeCriteria;
    }

    public MdServiceTypeCriteria removeMdServiceTypeCriteria(MdServiceTypeCriteria mdServiceTypeCriteria) {
        getMdServiceTypeCriteria().remove(mdServiceTypeCriteria);
        mdServiceTypeCriteria.setMdServiceType(null);

        return mdServiceTypeCriteria;
    }

    public MdServiceTypeRule addMdServiceTypeRule(MdServiceTypeRule mdServiceTypeRule) {
        getMdServiceTypeRules().add(mdServiceTypeRule);
        mdServiceTypeRule.setMdServiceType(this);

        return mdServiceTypeRule;
    }

    public MdServiceTypeRule removeMdServiceTypeRule(MdServiceTypeRule mdServiceTypeRule) {
        getMdServiceTypeRules().remove(mdServiceTypeRule);
        mdServiceTypeRule.setMdServiceType(null);

        return mdServiceTypeRule;
    }


    public Set<MdServiceItem> getServiceItems() {
        if (serviceItems == null)
            this.serviceItems = new HashSet<>();

        return serviceItems;
    }

    public MdServiceItem addServiceItem(MdServiceItem mdServiceItem) {
        getServiceItems().add(mdServiceItem);
        return mdServiceItem;
    }

    public void addServiceItems(Set<MdServiceItem> mdServiceItems) {
        getServiceItems().addAll(mdServiceItems);
    }

    public List<MdServiceTypeCriteria> getMdServiceTypeCriteria() {
        if (Objects.isNull(mdServiceTypeCriteria))
            mdServiceTypeCriteria = new ArrayList<>();
        return mdServiceTypeCriteria;
    }

    public List<MdServiceTypeRule> getMdServiceTypeRules() {
        if (Objects.isNull(mdServiceTypeRules))
            mdServiceTypeRules = new ArrayList<>();
        return mdServiceTypeRules;
    }


    public boolean removeServiceItems(Set<MdServiceItem> mdServiceItems) {
        return getServiceItems().removeAll(mdServiceItems);
    }
}