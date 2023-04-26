package com.automate.vehicleservices.entity;

import com.automate.vehicleservices.api.model.AddressLabel;
import com.automate.vehicleservices.entity.enums.ServiceAppointmentStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.*;
import java.io.Serializable;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


/**
 * The persistent class for the service_appointment database table.
 */
@Setter
@Getter
@Entity
@Table(name = "service_appointment")
@NamedQuery(name = "ServiceAppointment.findAll", query = "SELECT s FROM ServiceAppointment s")
public class ServiceAppointment extends Auditable implements Serializable {
    private static final long serialVersionUID = -6689468933113907606L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private int id;
    @Column(unique = true, nullable = false)
    private int BOOKING_ID;

    @Column(name = "APPOINTMENT_CREATED_BY", length = 45)
    private String appointmentCreatedBy;

    @Column(name = "APPOINTMENT_TOKEN_NUMBER", length = 45)
    private String appointmentTokenNumber;

    @Column(name = "ASSIGNED_DRIVER", length = 45)
    private String assignedDriver;

    @Column(name = "CITY", length = 45)
    private String city;

    @Column(name = "SERVICE_CENTER_CODE", length = 45)
    private String serviceCenterCode;

    @Column(name = "ADVISOR_NAME", length = 45)
    private String advisorName;

    //bi-directional many-to-one association to Customer
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.DETACH})
    @JoinColumn(name = "CUSTOMER_ID")
    @JsonBackReference
    private Customer customer;

    @Column(name = "DROP_ADDRESS_LABEL", length = 10)
    @Enumerated(EnumType.STRING)
    private AddressLabel dropAddressLabel;

    @Column(name = "DROP_TIME")
    private LocalDateTime dropTime;

    @Enumerated(EnumType.STRING)
    private ServiceAppointmentStatus status;

    @Column(name = "IS_SELF_DRIVE", columnDefinition = "TINYINT")
    private Boolean isSelfDrive;

    @Column(name = "IS_DOOR_STEP_SERVICE", columnDefinition = "TINYINT")
    private Boolean doorStepService;

    @Column(name = "IS_DROP_REQUIRED", columnDefinition = "TINYINT")
    private Boolean dropRequired;

    @Column(name = "IS_PICK_REQUIRED", columnDefinition = "TINYINT")
    private Boolean pickupRequired;

    @Column(name = "MODE_OF_CONTACT", length = 45)
    private String modeOfContact;

    @Column(name = "MODIFIED_REASON", length = 45)
    private String modifiedReason;

    @Column(name = "PAYMENT_TYPE", length = 1)
    private String paymentType;

    @Column(name = "PICK_UP_ADDRESS_LABEL", length = 10)
    @Enumerated(EnumType.STRING)
    private AddressLabel pickUpAddressLabel;

    @Column(name = "PICKUP_TIME")
    private LocalDateTime pickupTime;

    @Column(name = "POINT_OF_CONTACT", length = 45)
    private String pointOfContact;

    @Column(length = 45)
    private String remarks;

    @Column(name = "SERVICE_DATE")
    private LocalDate serviceDate;

    @Column(name = "SOURCE_OF_APPOINTMENT", length = 255)
    private String sourceOfAppointment;

    //bi-directional many-to-one association to MdServiceType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_TYPE")
    private MdServiceType mdServiceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_CATEGORY")
    private MdServiceCategory mdServiceCategory;

    //bi-directional many-to-one association to MdTenant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_CENTER")
    private MdTenant mdTenant;

    @ManyToOne
    @JoinColumn(name = "DRIVER_ID")
    private Driver driver;

    //bi-directional many-to-one association to ServiceSlotAvailability
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.DETACH})
    @JoinColumn(name = "SLOT")
    private ServiceSlotAvailability serviceSlotAvailability;

    //bi-directional many-to-one association to ServiceAppointmentAdditionalService
    @OneToMany(mappedBy = "serviceAppointment")
    @JsonManagedReference
    private List<ServiceAppointmentAdditionalService> serviceAppointmentAdditionalServices = new ArrayList<>();

    //bi-directional many-to-one association to CustomerAddress
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.DETACH})
    @JoinColumn(name = "DROP_ADDRESS")
    private CustomerAddress dropAddress;

    //bi-directional many-to-one association to CustomerAddress
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.DETACH})
    @JoinColumn(name = "PICKUP_ADDRESS")
    private CustomerAddress pickupAddress;

    //bi-directional many-to-one association to ServiceVehicle
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.DETACH})
    @JoinColumn(name = "VEHICLE_ID")
    @JsonBackReference
    private ServiceVehicle serviceVehicle;

    //bi-directional many-to-one association to VehicleKmTracker
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.DETACH})
    @JoinColumn(name = "VEHICLE_KM_TRACKER")
    private VehicleKmTracker vehicleKmTrackerBean;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "service_appointment_document", joinColumns = {@JoinColumn(name = "APPOINTMENT_ID")},
            inverseJoinColumns = {@JoinColumn(name = "DOCUMENT_ID")})
    private Set<ServiceDocument> serviceDocuments = new HashSet<>();

    // Specifications
    @JsonIgnore
    public static Specification<ServiceAppointment> appointmentByOrgIdentifierSpecification(String org) {
        return (Specification<ServiceAppointment>) (root,
                                                    criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("mdTenant").get("mdOrganization").get("orgIdentifier"), org);
    }

    // Specifications
    @JsonIgnore
    public static Specification<ServiceAppointment> appointmentsByDateTodayOrLater() {
        return (Specification<ServiceAppointment>) (root,
                                                    criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(root.get("serviceDate"), LocalDate.now());
    }

    // Specifications
    @JsonIgnore
    public static Specification<ServiceAppointment> byTenantIdentifier(final String identifier) {
        return (Specification<ServiceAppointment>) (root,
                                                    criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("mdTenant").get("tenantIdentifier"), identifier);
    }

    @JsonIgnore
    public static Specification<ServiceAppointment> appointmentByCustomerContactNumber(String customerContactNumber) {

        final var format = MessageFormat.format("%{0}%", customerContactNumber);
        return (Specification<ServiceAppointment>) (root,
                                                    criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .like(root.get("customer").get("contactNumber"), format);
    }

    @JsonIgnore
    public static Specification<ServiceAppointment> compareMdServiceSlotId(Integer mdServiceSlotId) {
        return (Specification<ServiceAppointment>) (root,
                                                    criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("serviceSlotAvailability").get("mdServiceSlot").get("id"), mdServiceSlotId);
    }

    @JsonIgnore
    public static Specification<ServiceAppointment> checkStatusSpecification(List<ServiceAppointmentStatus> statusList) {
        return (Specification<ServiceAppointment>) (root,
                                                    criteriaQuery, criteriaBuilder) -> root.get("status").in(statusList);
    }

    public ServiceAppointmentAdditionalService addServiceAppointmentAdditionalService(
            ServiceAppointmentAdditionalService serviceAppointmentAdditionalService) {
        getServiceAppointmentAdditionalServices().add(serviceAppointmentAdditionalService);
        serviceAppointmentAdditionalService.setServiceAppointment(this);

        return serviceAppointmentAdditionalService;
    }

    public ServiceAppointmentAdditionalService removeServiceAppointmentAdditionalService(
            ServiceAppointmentAdditionalService serviceAppointmentAdditionalService) {
        getServiceAppointmentAdditionalServices().remove(serviceAppointmentAdditionalService);
        serviceAppointmentAdditionalService.setServiceAppointment(null);

        return serviceAppointmentAdditionalService;
    }

    public List<ServiceAppointmentAdditionalService> getServiceAppointmentAdditionalServices() {
        if (Objects.isNull(serviceAppointmentAdditionalServices))
            serviceAppointmentAdditionalServices = new ArrayList<>();
        return serviceAppointmentAdditionalServices;
    }
}