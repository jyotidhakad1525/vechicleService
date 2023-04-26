/**
 *
 */
package com.automate.vehicleservices.framework.validation;


import com.automate.vehicleservices.framework.api.APIError;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author chandrashekharv
 *
 */
@Data
@AllArgsConstructor
public class FieldValidationError implements APIError {

    private String field;
    private String message;
}
