package com.automate.vehicleservices.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberUtil {

    public static final int PHONE_NUMBER_MANDATORY_LENGTH_IGNORING_PLUS_AND_COUNTRY_CODE = 10;

    public String cleansePhoneNumber(String phoneNumber) {
        if (StringUtils.isNotBlank(phoneNumber)) {
            phoneNumber = phoneNumber.trim();
            if (phoneNumber.length() > PHONE_NUMBER_MANDATORY_LENGTH_IGNORING_PLUS_AND_COUNTRY_CODE)
                phoneNumber = phoneNumber
                        .substring(phoneNumber.length() - PHONE_NUMBER_MANDATORY_LENGTH_IGNORING_PLUS_AND_COUNTRY_CODE);
        }
        return phoneNumber;
    }
}
