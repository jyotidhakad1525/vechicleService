package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.api.model.AddressLabel;
import com.automate.vehicleservices.entity.*;
import com.automate.vehicleservices.entity.enums.ServiceAppointmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ServiceAppointmentBuilder {
    private int id;
    private String appointmentCreatedBy;
    private String appointmentTokenNumber;
    private String assignedDriver;
    private String city;
    //bi-directional many-to-one association to Customer
    private Customer customer;
    private AddressLabel dropAddressLabel;
    private LocalDateTime dropTime;
    private ServiceAppointmentStatus status;
    private Boolean isSelfDrive;
    private Boolean doorStepService;
    private Boolean dropRequired;
    private Boolean pickupRequired;
    private String modeOfContact;
    private String modifiedReason;
    private String paymentType;
    private AddressLabel pickUpAddressLabel;
    private LocalDateTime pickupTime;
    private String pointOfContact;
    private String remarks;
    private LocalDate serviceDate;
    private String sourceOfAppointment;
    //bi-directional many-to-one association to MdServiceType
    private MdServiceType mdServiceType;
    private MdServiceCategory mdServiceCategory;
    //bi-directional many-to-one association to MdTenant
    private MdTenant mdTenant;
    //bi-directional many-to-one association to ServiceSlotAvailability
    private ServiceSlotAvailability serviceSlotAvailability;
    //bi-directional many-to-one association to ServiceAppointmentAdditionalService
    private List<ServiceAppointmentAdditionalService> serviceAppointmentAdditionalServices = new ArrayList<>();
    //bi-directional many-to-one association to CustomerAddress
    private CustomerAddress dropAddress;
    //bi-directional many-to-one association to CustomerAddress
    private CustomerAddress pickupAddress;
    //bi-directional many-to-one association to ServiceVehicle
    private ServiceVehicle serviceVehicle;
    //bi-directional many-to-one association to VehicleKmTracker
    private VehicleKmTracker vehicleKmTrackerBean;
    private Set<ServiceDocument> serviceDocuments = new HashSet<>();
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private Driver driver;

    private ServiceAppointmentBuilder() {
    }

    public static ServiceAppointmentBuilder aServiceAppointment() {
        return new ServiceAppointmentBuilder();
    }

    public ServiceAppointmentBuilder withDriver(Driver driver) {
        this.driver = driver;
        return this;
    }

    public ServiceAppointmentBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public ServiceAppointmentBuilder withAppointmentCreatedBy(String appointmentCreatedBy) {
        this.appointmentCreatedBy = appointmentCreatedBy;
        return this;
    }

    public ServiceAppointmentBuilder withAppointmentTokenNumber(String appointmentTokenNumber) {
        this.appointmentTokenNumber = appointmentTokenNumber;
        return this;
    }

    public ServiceAppointmentBuilder withAssignedDriver(String assignedDriver) {
        this.assignedDriver = assignedDriver;
        return this;
    }

    public ServiceAppointmentBuilder withCity(String city) {
        this.city = city;
        return this;
    }

    public ServiceAppointmentBuilder withCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public ServiceAppointmentBuilder withDropAddressLabel(AddressLabel dropAddressLabel) {
        this.dropAddressLabel = dropAddressLabel;
        return this;
    }

    public ServiceAppointmentBuilder withDropTime(LocalDateTime dropTime) {
        this.dropTime = dropTime;
        return this;
    }

    public ServiceAppointmentBuilder withStatus(ServiceAppointmentStatus status) {
        this.status = status;
        return this;
    }

    public ServiceAppointmentBuilder withIsSelfDrive(Boolean isSelfDrive) {
        this.isSelfDrive = isSelfDrive;
        return this;
    }

    public ServiceAppointmentBuilder withDoorStepService(Boolean doorStepService) {
        this.doorStepService = doorStepService;
        return this;
    }

    public ServiceAppointmentBuilder withDropRequired(Boolean dropRequired) {
        this.dropRequired = dropRequired;
        return this;
    }

    public ServiceAppointmentBuilder withPickupRequired(Boolean pickupRequired) {
        this.pickupRequired = pickupRequired;
        return this;
    }

    public ServiceAppointmentBuilder withModeOfContact(String modeOfContact) {
        this.modeOfContact = modeOfContact;
        return this;
    }

    public ServiceAppointmentBuilder withModifiedReason(String modifiedReason) {
        this.modifiedReason = modifiedReason;
        return this;
    }

    public ServiceAppointmentBuilder withPaymentType(String paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public ServiceAppointmentBuilder withPickUpAddressLabel(AddressLabel pickUpAddressLabel) {
        this.pickUpAddressLabel = pickUpAddressLabel;
        return this;
    }

    public ServiceAppointmentBuilder withPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
        return this;
    }

    public ServiceAppointmentBuilder withPointOfContact(String pointOfContact) {
        this.pointOfContact = pointOfContact;
        return this;
    }

    public ServiceAppointmentBuilder withRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public ServiceAppointmentBuilder withServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
        return this;
    }

    public ServiceAppointmentBuilder withSourceOfAppointment(String sourceOfAppointment) {
        this.sourceOfAppointment = sourceOfAppointment;
        return this;
    }

    public ServiceAppointmentBuilder withMdServiceType(MdServiceType mdServiceType) {
        this.mdServiceType = mdServiceType;
        return this;
    }

    public ServiceAppointmentBuilder withMdServiceCategory(MdServiceCategory mdServiceCategory) {
        this.mdServiceCategory = mdServiceCategory;
        return this;
    }

    public ServiceAppointmentBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public ServiceAppointmentBuilder withServiceSlotAvailability(ServiceSlotAvailability serviceSlotAvailability) {
        this.serviceSlotAvailability = serviceSlotAvailability;
        return this;
    }

    public ServiceAppointmentBuilder withServiceAppointmentAdditionalServices(
            List<ServiceAppointmentAdditionalService> serviceAppointmentAdditionalServices) {
        this.serviceAppointmentAdditionalServices = serviceAppointmentAdditionalServices;
        return this;
    }

    public ServiceAppointmentBuilder withDropAddress(CustomerAddress dropAddress) {
        this.dropAddress = dropAddress;
        return this;
    }

    public ServiceAppointmentBuilder withPickupAddress(CustomerAddress pickupAddress) {
        this.pickupAddress = pickupAddress;
        return this;
    }

    public ServiceAppointmentBuilder withServiceVehicle(ServiceVehicle serviceVehicle) {
        this.serviceVehicle = serviceVehicle;
        return this;
    }

    public ServiceAppointmentBuilder withVehicleKmTrackerBean(VehicleKmTracker vehicleKmTrackerBean) {
        this.vehicleKmTrackerBean = vehicleKmTrackerBean;
        return this;
    }

    public ServiceAppointmentBuilder withServiceDocuments(Set<ServiceDocument> serviceDocuments) {
        this.serviceDocuments = serviceDocuments;
        return this;
    }

    public ServiceAppointmentBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ServiceAppointmentBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public ServiceAppointmentBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public ServiceAppointmentBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public ServiceAppointment build() {
        ServiceAppointment serviceAppointment = new ServiceAppointment();
        serviceAppointment.setId(id);
        serviceAppointment.setAppointmentCreatedBy(appointmentCreatedBy);
        serviceAppointment.setAppointmentTokenNumber(appointmentTokenNumber);
        serviceAppointment.setAssignedDriver(assignedDriver);
        serviceAppointment.setCity(city);
        serviceAppointment.setCustomer(customer);
        serviceAppointment.setDropAddressLabel(dropAddressLabel);
        serviceAppointment.setDropTime(dropTime);
        serviceAppointment.setStatus(status);
        serviceAppointment.setIsSelfDrive(isSelfDrive);
        serviceAppointment.setDoorStepService(doorStepService);
        serviceAppointment.setDropRequired(dropRequired);
        serviceAppointment.setPickupRequired(pickupRequired);
        serviceAppointment.setModeOfContact(modeOfContact);
        serviceAppointment.setModifiedReason(modifiedReason);
        serviceAppointment.setPaymentType(paymentType);
        serviceAppointment.setPickUpAddressLabel(pickUpAddressLabel);
        serviceAppointment.setPickupTime(pickupTime);
        serviceAppointment.setPointOfContact(pointOfContact);
        serviceAppointment.setRemarks(remarks);
        serviceAppointment.setServiceDate(serviceDate);
        serviceAppointment.setSourceOfAppointment(sourceOfAppointment);
        serviceAppointment.setMdServiceType(mdServiceType);
        serviceAppointment.setMdServiceCategory(mdServiceCategory);
        serviceAppointment.setMdTenant(mdTenant);
        serviceAppointment.setServiceSlotAvailability(serviceSlotAvailability);
        serviceAppointment.setServiceAppointmentAdditionalServices(serviceAppointmentAdditionalServices);
        serviceAppointment.setDropAddress(dropAddress);
        serviceAppointment.setPickupAddress(pickupAddress);
        serviceAppointment.setServiceVehicle(serviceVehicle);
        serviceAppointment.setVehicleKmTrackerBean(vehicleKmTrackerBean);
        serviceAppointment.setServiceDocuments(serviceDocuments);
        serviceAppointment.setCreatedBy(createdBy);
        serviceAppointment.setCreatedDate(createdDate);
        serviceAppointment.setLastModifiedBy(lastModifiedBy);
        serviceAppointment.setLastModifiedDate(lastModifiedDate);
        serviceAppointment.setDriver(driver);
        return serviceAppointment;
    }
}
