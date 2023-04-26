package com.automate.vehicleservices.api.model.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VehicleHistoryRequest {

    private Integer id;

    private String serviceManager;

    private String complaintStatus;

    private String lastServiceFeedback;

    private String reasonForComplaint;

    private String dealerName;

    private String dealerLocation;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @PastOrPresent
    private LocalDate serviceDate;

    @PositiveOrZero
    private Integer kmReadingAtService;

    private String serviceCenter;

    @PositiveOrZero
    private Float serviceAmount;

    @PositiveOrZero
    private Integer serviceType;

    @PositiveOrZero
    private Integer subServiceType;
}
