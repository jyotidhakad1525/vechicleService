package com.automate.vehicleservices.api.model;

import com.automate.vehicleservices.api.validator.ServiceHistoryRequestDataIntegrityValidatorByRuleEngine;
import com.automate.vehicleservices.framework.validation.DataIntegrityCheck;
import com.automate.vehicleservices.framework.validation.ValidationRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DataIntegrityCheck(classes =
        ServiceHistoryRequestDataIntegrityValidatorByRuleEngine.class)
@Builder
public class VehicleServiceHistoryRequest implements ValidationRequest {
    @PositiveOrZero
    private int kmReadingAtService;
    private String information;
    @PositiveOrZero
    private float serviceAmount;
    private String serviceCenter;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @PastOrPresent
    private LocalDate serviceDate;
    private String serviceManager; 
    private String complaintStatus;
	private String dealerName;
    private String dealerLocation;
    private String lastServiceFeedback;
    private String reasonForComplaint;

    
    @PositiveOrZero
    private int serviceType;
    @NotNull
    private VehicleDetails vehicleDetails;
    @NotNull
    private CustomerRequest customer;
}
