package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.api.model.AddressLabel;
import com.automate.vehicleservices.entity.ServiceAppointment;
import com.automate.vehicleservices.entity.enums.ServiceAppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Chandrashekar V
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ServiceAppointmentResponse {
    private int responseId;
    private ServiceAppointmentStatus serviceAppointmentStatus;
    private String city;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String email;
    private String vehicleRegNumber;
    private LocalDate purchaseDate;
    private LocalDate serviceDate;
    private Slot slot;
    private CustomerAddressDTO dropAddress;
    private AddressLabel dropAddressLabel;
    private LocalDateTime dropTime;
    private CustomerAddressDTO pickupAddress;
    private AddressLabel pickUpAddressLabel;
    private Boolean isSelfDrive;
    private Boolean doorStepService;
    private LocalDateTime pickupTime;
    private String serviceName;
    private String categoryName;
    private String tenantIdentifier;
    private List<String> additionalServices;
    private String bookingNumber;

    private CommonIdNameDTO driverDetails;
    private String serviceCenterCode;
    private String advisorName;
    private String reason;
    private String remark;

    public ServiceAppointmentResponse(ServiceAppointment appointment) {
        this.city = appointment.getCity();
        this.serviceCenterCode = appointment.getServiceCenterCode();
        this.advisorName = appointment.getAdvisorName();
        this.reason = appointment.getModifiedReason();
        this.remark = appointment.getRemarks();
        this.bookingNumber = "BKG-" + appointment.getBOOKING_ID();
        final var driver = appointment.getDriver();
        if (Objects.nonNull(driver)) {
            this.driverDetails = new CommonIdNameDTO(driver.getId(), driver.getDriverName());
        }
        final var customer = appointment.getCustomer();
        if (Objects.nonNull(customer)) {
            this.firstName = customer.getFirstName();
            this.lastName = customer.getLastName();
            this.contactNumber = customer.getContactNumber();
            this.email = customer.getEmail();
        }
        final var serviceVehicle = appointment.getServiceVehicle();
        if (Objects.nonNull(serviceVehicle)) {
            this.vehicleRegNumber = serviceVehicle.getRegNumber();
            this.purchaseDate = serviceVehicle.getPurchaseDate();
        }
        this.serviceAppointmentStatus = appointment.getStatus();
        this.serviceDate = appointment.getServiceDate();
        if (Objects.nonNull(appointment.getDropAddress()))
            this.dropAddress = new CustomerAddressDTO(appointment.getDropAddress());
        this.dropAddressLabel = appointment.getDropAddressLabel();
        this.dropTime = appointment.getDropTime();
        if (Objects.nonNull(appointment.getPickupAddress()))
            this.pickupAddress = new CustomerAddressDTO(appointment.getPickupAddress());
        this.pickUpAddressLabel = appointment.getPickUpAddressLabel();
        this.pickupTime = appointment.getPickupTime();
        final var mdServiceType = appointment.getMdServiceType();
        if (Objects.nonNull(mdServiceType))
            this.serviceName = mdServiceType.getServiceName();
        final var mdServiceCategory = appointment.getMdServiceCategory();
        if (Objects.nonNull(mdServiceCategory))
            this.categoryName = mdServiceCategory.getCategoryName();
        final var mdTenant = appointment.getMdTenant();
        if (Objects.nonNull(mdTenant))
            this.tenantIdentifier = mdTenant.getTenantIdentifier();
        final var serviceAppointmentAdditionalServices = appointment.getServiceAppointmentAdditionalServices();
        if (Objects.nonNull(serviceAppointmentAdditionalServices))
            this.additionalServices = serviceAppointmentAdditionalServices.stream()
                    .map(serviceAppointmentAdditionalService -> serviceAppointmentAdditionalService.getMdServiceItem()
                            .getName()).collect(
                            Collectors.toList());

        this.isSelfDrive = appointment.getIsSelfDrive();
        this.doorStepService = appointment.getDoorStepService();

        this.responseId = appointment.getId();
        this.slot = new Slot(appointment.getServiceSlotAvailability());
    }
}
