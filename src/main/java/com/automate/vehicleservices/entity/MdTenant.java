package com.automate.vehicleservices.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


/**
 * The persistent class for the md_tenant database table.
 */
@Setter
@Getter
@Entity
@Table(name = "md_tenant")
@NamedQuery(name = "MdTenant.findAll", query = "SELECT m FROM MdTenant m")
public class MdTenant extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(length = 1000)
    private String address;

    @Column(length = 255)
    private String area;

    @Column(length = 255)
    private String city;

    @Column(name = "MASTER_IDENTIFIER")
    private Integer masterIdentifier;

    @Column(name = "IS_ACTIVE", columnDefinition = "TINYINT")
    private Boolean isActive;

    @Column(name = "TENANT_IDENTIFIER", unique = true, nullable = false, length = 20)
    @NaturalId
    private String tenantIdentifier;

    @Column(name = "TENANT_NAME", nullable = false, length = 255)
    private String tenantName;

    @Column(name = "TENANT_SHORT_CODE", length = 20)
    private String tenantShortCode;

    //bi-directional many-to-one association to MdOrganization
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_IDENTIFIER")
    @JsonBackReference
    private MdOrganization mdOrganization;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "mdTenant")
    private Set<CustomerTenant> customerTenants = new HashSet<>();

    @JsonIgnore
    public Set<CustomerTenant> getCustomerTenants() {
        if (null == this.customerTenants)
            customerTenants = new HashSet<>();
        return customerTenants;
    }

    public CustomerTenant addCustomerTenant(CustomerTenant customerTenant) {
        Set<CustomerTenant> customerTenants = getCustomerTenants();
        Optional<CustomerTenant> any = customerTenants.stream()
                .filter(ct -> StringUtils.equals(ct.getMdTenant().getTenantIdentifier(),
                        customerTenant.getMdTenant().getTenantIdentifier())
                        && ct.getCustomer().getId() == customerTenant.getCustomer().getId()).findAny();

        if (any.isEmpty()) {
            customerTenants.add(customerTenant);
            customerTenant.setMdTenant(this);
        }
        return customerTenant;
    }

    public CustomerTenant removeCustomerTenant(CustomerTenant customerTenant) {
        getCustomerTenants().remove(customerTenant);
        customerTenant.setMdTenant(null);

        return customerTenant;
    }
}