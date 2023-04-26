package com.automate.vehicleservices.api.model.followup;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public enum CustomerNotInterestedReasons implements FollowUpClosingAction {
    VEHICLE_SOLD,
    PURCHASED_ANOTHER_CAR,
    TRANSFER_TO_ANOTHER_CITY,
    TOO_FAR_FROM_DEALER_SCENARIO,
    ALREADY_SERVICED,
    OUT_OF_STATION,
    EXPECTING_MORE_DISCOUNT,
    EXCESS_BILLING,
    CNG_VEHICLE,
    TOTAL_LOSS_OR_DAMAGE,
    NO_SPECIFIC_REASON,
    CUSTOMER_DISSATISFIED,
    NO_RESPONSE,
    OTHERS;

    public static Optional<CustomerNotInterestedReasons> getEnum(String value) {
        for (CustomerNotInterestedReasons enumValue : CustomerNotInterestedReasons.values()) {
            if (StringUtils.equalsIgnoreCase(value, enumValue.name()))
                return Optional.of(enumValue);
        }
        return Optional.empty();
    }
}
