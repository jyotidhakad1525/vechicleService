package com.automate.vehicleservices.api.model;

import com.automate.vehicleservices.api.validator.AppointmentRequestDataIntegrityValidatorByRuleEngine;
import com.automate.vehicleservices.framework.validation.DataIntegrityCheck;
import com.automate.vehicleservices.framework.validation.ValidationRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Chandrashekar V
 */
@Setter
@Getter
@DataIntegrityCheck(classes =
        AppointmentRequestDataIntegrityValidatorByRuleEngine.class)
public class AppointmentRequest implements ValidationRequest {

    private Integer id;
    private String city;

    @FutureOrPresent(message = "Service date can not be in the past.")
    private LocalDate serviceRequestDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime requestedTime = LocalDateTime.now();

    @Positive
    private int timeSlotId;

    private Integer driverId;

    @NotNull
    @Valid
    private VehicleDetails vehicleDetails;

    @Positive(message = "Invalid service Category")
    private int serviceCategoryId;

    private Integer subServiceId;

    private String reason;
    private String remark;
    private String serviceCenterCode;
    private String advisorName;

    private boolean isPickupRequired;

    private boolean isDropRequired;

    private AddressRequest pickupAddress;

    private LocalDateTime pickupTime;

    private LocalDateTime dropTime;

    private boolean isSelfDrive;

    private boolean doorStepService;

    private AddressRequest dropAddress;

    private String sourceOfRequest;

    private CustomerRequest customerRequest;

    @Email
    private String email;

    private List<AdditionalServiceRequest> additionalServices;

    private List<DocContent> documents;

    public boolean isPickupRequired() {
        return isPickupRequired;
    }

    public void setPickupRequired(boolean pickupRequired) {
        isPickupRequired = pickupRequired;
    }

    public boolean isDropRequired() {
        return isDropRequired;
    }

    public void setDropRequired(boolean dropRequired) {
        isDropRequired = dropRequired;
    }


}
