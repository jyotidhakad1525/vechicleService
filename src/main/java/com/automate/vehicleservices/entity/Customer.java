package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.entity.enums.Gender;
import com.automate.vehicleservices.entity.enums.Salutation;
import com.automate.vehicleservices.service.dto.SearchResponseDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;


/**
 * The persistent class for the customer database table.
 */
@SqlResultSetMapping(name = "CustomerSearchResult", classes = {
        @ConstructorResult(
                targetClass = SearchResponseDTO.class,
                columns = {
                        @ColumnResult(name = "customerId", type = Integer.class),
                        @ColumnResult(name = "firstName", type = String.class),
                        @ColumnResult(name = "lastName", type = String.class),
                        @ColumnResult(name = "vehicleRegNumber", type = String.class),
                        @ColumnResult(name = "chassisNumber", type = String.class),
                        @ColumnResult(name = "vin", type = String.class),
                        @ColumnResult(name = "engineNumber", type = String.class),
                        @ColumnResult(name = "policyNumber", type = String.class),
                        @ColumnResult(name = "model", type = String.class),
                        @ColumnResult(name = "contactNumber", type = String.class)
                }
        )
})
@Entity
@Table(name = "customer")
@NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c")
@Getter
@Setter
public class Customer extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "SALUTATION")
    private Salutation salutation;

    private String relationName;

    private Integer age;


    @Column(name = "ALT_CONTACT_NUMBER", length = 20)
    private String altContactNumber;

    @Deprecated
    @Column(name = "COMMUNICATION_PREFERENCE", length = 1)
    private String communicationPreference;

    @Column(name = "CONTACT_NUMBER", nullable = false, length = 20, unique = true)
    private String contactNumber;

    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

    @Column(name = "DATE_OF_ARRIVAL")
    private LocalDate dateOfArrival;

    @Column(length = 255)
    private String email;

    @Column(length = 255)
    private String customerType;

    @Column(length = 255)
    private String occupation;

    @Column(name = "FIRST_NAME", nullable = false, length = 255)
    private String firstName;

    @Deprecated
    @Column(length = 255)
    private String refered_by;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER")
    private Gender gender;

    @Column(name = "IS_ACTIVE", columnDefinition = "TINYINT")
    private Boolean active;

    @Column(name = "LAST_NAME", nullable = false, length = 255)
    private String lastName;

    //bi-directional many-to-one association to CustomerAddress
    @OneToMany(mappedBy = "customer", cascade = {CascadeType.ALL})
    @JsonManagedReference
    private List<CustomerAddress> customerAddresses = new ArrayList<>();

    //bi-directional many-to-one association to CustomerVehicle
    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    private List<ServiceVehicle> customerVehicles = new ArrayList<>();

    //bi-directional many-to-one association to ServiceDocument
    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    private List<ServiceDocument> serviceDocuments = new ArrayList<>();

    //bi-directional many-to-one association to ServiceEstimateRequest
    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    private List<ServiceEstimate> serviceEstimates = new ArrayList<>();

    //bi-directional many-to-one association to ServiceEstimateRequest
    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    private List<ServiceAppointment> serviceAppointments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LEAD_SOURCE")
    private MdLeadSource leadSource;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "customer")
    private Set<CustomerTenant> customerTenants = new HashSet<>();

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
            customerTenant.setCustomer(this);
        }
        return customerTenant;
    }

    public CustomerTenant removeCustomerTenant(CustomerTenant customerTenant) {
        getCustomerTenants().remove(customerTenant);
        customerTenant.setCustomer(null);

        return customerTenant;
    }

    public List<CustomerAddress> getCustomerAddresses() {
        if (this.customerAddresses == null)
            customerAddresses = new ArrayList<>();
        return this.customerAddresses;
    }

    public CustomerAddress addCustomerAddress(CustomerAddress customerAddress) {
        getCustomerAddresses().add(customerAddress);
        customerAddress.setCustomer(this);

        return customerAddress;
    }

    public CustomerAddress removeCustomerAddress(CustomerAddress customerAddress) {
        if (null != this.customerAddresses) {
            getCustomerAddresses().remove(customerAddress);
            customerAddress.setCustomer(null);
        }
        return customerAddress;
    }


    public List<ServiceDocument> getServiceDocuments() {
        if (this.serviceDocuments == null)
            serviceDocuments = new ArrayList<>();

        return this.serviceDocuments;
    }

    public ServiceDocument addServiceDocument(ServiceDocument serviceDocument) {

        getServiceDocuments().add(serviceDocument);
        serviceDocument.setCustomer(this);

        return serviceDocument;
    }

    public ServiceDocument removeServiceDocument(ServiceDocument serviceDocument) {
        getServiceDocuments().remove(serviceDocument);
        serviceDocument.setCustomer(null);

        return serviceDocument;
    }

    public List<ServiceEstimate> getServiceEstimates() {
        if (this.serviceEstimates == null)
            serviceEstimates = new ArrayList<>();
        return this.serviceEstimates;
    }

    public ServiceEstimate addServiceEstimateRequest(ServiceEstimate serviceEstimate) {
        getServiceEstimates().add(serviceEstimate);
        serviceEstimate.setCustomer(this);

        return serviceEstimate;
    }

    public ServiceEstimate removeServiceEstimateRequest(ServiceEstimate serviceEstimate) {
        getServiceEstimates().remove(serviceEstimate);
        serviceEstimate.setCustomer(null);

        return serviceEstimate;
    }


    public List<ServiceVehicle> getCustomerVehicles() {
        if (this.customerVehicles == null)
            customerVehicles = new ArrayList<>();
        return customerVehicles;
    }

    public ServiceVehicle addCustomerVehicle(ServiceVehicle serviceVehicle) {
        getCustomerVehicles().add(serviceVehicle);
        serviceVehicle.setCustomer(this);

        return serviceVehicle;
    }

    public ServiceVehicle removeCustomerVehicle(ServiceVehicle serviceVehicle) {
        getCustomerVehicles().remove(serviceVehicle);
        serviceVehicle.setCustomer(null);

        return serviceVehicle;
    }

    public List<ServiceAppointment> getServiceAppointments() {
        if (this.serviceAppointments == null)
            serviceAppointments = new ArrayList<>();
        return serviceAppointments;
    }


    public ServiceAppointment addServiceEstimateRequest(ServiceAppointment serviceAppointment) {
        getServiceAppointments().add(serviceAppointment);
        serviceAppointment.setCustomer(this);

        return serviceAppointment;
    }

    public ServiceAppointment removeServiceEstimateRequest(ServiceAppointment serviceAppointment) {
        getServiceAppointments().remove(serviceAppointment);
        serviceAppointment.setCustomer(null);

        return serviceAppointment;
    }

    public String getFullName() {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(this.firstName)) {
            stringBuilder.append(this.firstName);
        }

        if (StringUtils.isNotBlank(this.lastName)) {
            stringBuilder.append(StringUtils.SPACE).append(this.lastName);
        }

        return stringBuilder.toString();
    }
}