package com.automate.vehicleservices.api.model.builder;

import com.automate.vehicleservices.service.dto.ValidationError;
import com.automate.vehicleservices.service.dto.ValidationResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Validation Response builder.
 */
public final class ValidationResponseBuilder {
    private String message;
    private List<ValidationError> errors = new ArrayList<>();
    private boolean isSuccess;

    private ValidationResponseBuilder() {
    }

    public static ValidationResponseBuilder aValidationResponse() {
        return new ValidationResponseBuilder();
    }

    public void withIsSuccess(boolean success) {
        isSuccess = success;
    }

    public ValidationResponseBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    public ValidationResponseBuilder withErrors(List<ValidationError> errors) {
        this.errors = errors;
        return this;
    }

    public ValidationResponseBuilder withError(ValidationError error) {
        this.errors.add(error);
        return this;
    }

    public ValidationResponse build() {
        ValidationResponse validationResponse = new ValidationResponse();
        validationResponse.setMessage(message);
        validationResponse.setErrors(errors);
        isSuccess = errors.isEmpty();
        validationResponse.setSuccess(isSuccess);
        return validationResponse;
    }
}
