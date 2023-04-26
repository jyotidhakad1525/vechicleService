package com.automate.vehicleservices.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Holds validation response.
 */
@Getter
@Setter
public class ValidationResponse {

    private boolean isSuccess;

    private String message;

    private List<ValidationError> errors;


}
