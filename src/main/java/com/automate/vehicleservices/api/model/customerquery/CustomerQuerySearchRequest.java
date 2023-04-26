package com.automate.vehicleservices.api.model.customerquery;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerQuerySearchRequest {
    private int id;

    private String assignedTo;

    private String createdBy;
}
