package com.automate.vehicleservices.service.dto;

import lombok.*;

/**
 * Holds validation error.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ValidationError {

    private String error;

    private String field;
}
