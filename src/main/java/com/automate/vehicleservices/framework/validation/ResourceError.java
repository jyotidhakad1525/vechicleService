package com.automate.vehicleservices.framework.validation;

import com.automate.vehicleservices.framework.api.APIError;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Chandrashekar V
 */
@Data
@AllArgsConstructor
public class ResourceError implements APIError {

    private String resource;
    private String id;
    private String message;

}
