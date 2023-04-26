package com.automate.vehicleservices.api.model.followup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "reason",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = VehicleSoldScenario.class, name = "VEHICLE_SOLD"),
        @JsonSubTypes.Type(value = TransferToAnotherCityScenario.class, name = "TRANSFER_TO_ANOTHER_CITY"),
        @JsonSubTypes.Type(value = PurchasedAnotherCarScenario.class, name = "PURCHASED_ANOTHER_CAR"),
        @JsonSubTypes.Type(value = AlreadyServicedScenario.class, name = "ALREADY_SERVICED"),
        @JsonSubTypes.Type(value = TooFarFromDealerScenario.class, name = "TOO_FAR_FROM_DEALER_SCENARIO"),
        @JsonSubTypes.Type(value = NoFurtherActionButCaptureRemarksCategory.class, name = "OUT_OF_STATION"),
        @JsonSubTypes.Type(value = NoFurtherActionButCaptureRemarksCategory.class, name = "NO_SPECIFIC_REASON"),
        @JsonSubTypes.Type(value = NoFurtherActionButCaptureRemarksCategory.class, name = "EXCESS_BILLING"),
        @JsonSubTypes.Type(value = NoFurtherActionButCaptureRemarksCategory.class, name = "CNG_VEHICLE"),
        @JsonSubTypes.Type(value = NoFurtherActionButCaptureRemarksCategory.class, name = "EXPECTING_MORE_DISCOUNT"),
        @JsonSubTypes.Type(value = NoFurtherActionButCaptureRemarksCategory.class, name = "TOTAL_LOSS_OR_DAMAGE"),
        @JsonSubTypes.Type(value = NoFurtherActionButCaptureRemarksCategory.class, name = "OTHERS"),
        @JsonSubTypes.Type(value = AppointmentBookingScenario.class, name = "APPOINTMENT_BOOKING"),
        @JsonSubTypes.Type(value = CustomerDissatisfiedScenario.class, name = "CUSTOMER_DISSATISFIED"),
        @JsonSubTypes.Type(value = NoResponseScenario.class, name = "NO_RESPONSE")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FollowUpResultCapture {
    private String reason;
    private String creRemarks;

    @JsonIgnore
    public boolean isAsync() {
        return true;
    }
}
