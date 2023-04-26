package com.automate.vehicleservices.service;

import com.automate.vehicleservices.service.dto.ValidationResponse;

public interface VehicleServiceValidator<R> {

    ValidationResponse validate(R r);
}
