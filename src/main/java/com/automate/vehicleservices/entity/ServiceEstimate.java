package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.api.model.PaymentType;
import com.automate.vehicleservices.entity.enums.ServiceGroup;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.*;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.*;


/**
 * The persistent class for the service_estimate_request database table.
 */
@Setter
@Getter
@Entity
@Table(name = "service_estimate_request")
@NamedQuery(name = "ServiceEstimate.findAll", query = "SELECT s FROM ServiceEstimate s")
public class ServiceEstimate extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(name = "SERVICE_GROUP", nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceGroup serviceGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID")
    private MdTenant mdTenant;

    //bi-directional many-to-one association to ServiceAppointmentAdditionalService
    @OneToMany(mappedBy = "serviceEstimate")
    @JsonManagedReference
    private List<ServiceEstimateAdditionalService> serviceEstimateAdditionalServices;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "service_estimate_document", joinColumns = {@JoinColumn(name = "ESTIMATE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "DOCUMENT_ID")})
    private Set<ServiceDocument> serviceDocuments = new HashSet<>();

    //bi-directional many-to-one association to Customer
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.REFRESH})
    @JoinColumn(name = "CUSTOMER_ID")
    @JsonBackReference
    private Customer customer;

    //bi-directional many-to-one association to VehicleKmTracker
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VEHICLE_KM_TRACKER")
    @JsonBackReference
    private VehicleKmTracker vehicleKmTracker;

    //bi-directional many-to-one association to ServiceVehicle
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.REFRESH})
    @JoinColumn(name = "VEHICLE_ID")
    private ServiceVehicle serviceVehicle;

    //bi-directional one-to-one association to ServiceEstimateResponse
    @OneToOne(mappedBy = "serviceEstimate", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.REFRESH})
    @JsonManagedReference
    private ServiceEstimateResponse serviceEstimateResponse;

    @Column(name = "PAYMENT_TYPE")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @JsonIgnore
    public static Specification<ServiceEstimate> estimateByCustomerContactNumber(String customerContactNumber) {

        final var format = MessageFormat.format("%{0}%", customerContactNumber);
        return (Specification<ServiceEstimate>) (root,
                                                 criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .like(root.get("customer").get("contactNumber"), format);
    }

    @JsonIgnore
    public static Specification<ServiceEstimate> estimateByOrgIdentifierSpecification(String org) {
        return (Specification<ServiceEstimate>) (root,
                                                 criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("mdTenant").get("mdOrganization").get("orgIdentifier"), org);
    }

    @JsonIgnore
    public static Specification<ServiceEstimate> estimateByServiceGroupSpecification(ServiceGroup serviceGroup) {
        return (Specification<ServiceEstimate>) (root,
                                                 criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(
                "serviceGroup"), serviceGroup);
    }

    public void setServiceEstimateResponse(ServiceEstimateResponse serviceEstimateResponse) {
        this.serviceEstimateResponse = serviceEstimateResponse;
        if (serviceEstimateResponse != null) {
            serviceEstimateResponse.setServiceEstimate(this);
        }
    }

    public List<ServiceEstimateAdditionalService> getServiceEstimateAdditionalServices() {
        if (Objects.isNull(serviceEstimateAdditionalServices))
            serviceEstimateAdditionalServices = new ArrayList<>();
        return serviceEstimateAdditionalServices;
    }

}