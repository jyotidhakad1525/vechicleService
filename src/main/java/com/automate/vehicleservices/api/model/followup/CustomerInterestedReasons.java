package com.automate.vehicleservices.api.model.followup;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public enum CustomerInterestedReasons implements FollowUpClosingAction {
    APPOINTMENT_BOOKING;

    public static Optional<CustomerInterestedReasons> getEnum(String value) {
        for (CustomerInterestedReasons enumValue : CustomerInterestedReasons.values()) {
            if (StringUtils.equalsIgnoreCase(value, enumValue.name()))
                return Optional.of(enumValue);
        }
        return Optional.empty();
    }
}
