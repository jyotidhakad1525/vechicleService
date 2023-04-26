package com.automate.vehicleservices.framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author chandrashekharv
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VehicleServicesException extends ResponseStatusException {

    private static final long serialVersionUID = 7439332653823544153L;


    public VehicleServicesException(HttpStatus status) {
        super(status);
    }

    public VehicleServicesException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public VehicleServicesException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }

    public VehicleServicesException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

}
