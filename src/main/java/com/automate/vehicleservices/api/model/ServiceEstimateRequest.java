package com.automate.vehicleservices.api.model;

import com.automate.vehicleservices.api.validator.DocContentDataIntegrityValidator;
import com.automate.vehicleservices.entity.enums.ServiceGroup;
import com.automate.vehicleservices.framework.validation.DataIntegrityCheck;
import com.automate.vehicleservices.framework.validation.ValidationRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Chandrashekar V
 */
@Getter
@Setter
@DataIntegrityCheck(classes = {DocContentDataIntegrityValidator.class})
public class ServiceEstimateRequest implements ValidationRequest {

    private String city;

    @NotNull
    @Valid
    private VehicleDetails vehicleDetails;

    private List<AdditionalServiceRequest> additionalServices;

    private List<DocContent> documents;

    @NotNull
    @Valid
    private CustomerRequest customer;

    @NotNull
    private ServiceGroup serviceType;

    private LocalDateTime requestTime = LocalDateTime.now();

    private String sourceOfRequest;

    private PaymentType paymentType;

    @Valid
    private VehicleInsuranceDetails vehicleInsuranceDetails;
}
