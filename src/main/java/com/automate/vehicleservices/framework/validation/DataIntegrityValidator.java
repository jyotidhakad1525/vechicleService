package com.automate.vehicleservices.framework.validation;

import com.automate.vehicleservices.service.dto.ValidationResponse;

/**
 * All data integrity constraint validator implementations must implement this interface.
 */
public interface DataIntegrityValidator {
    ValidationResponse validate(ValidationRequest baseRequest);

}
