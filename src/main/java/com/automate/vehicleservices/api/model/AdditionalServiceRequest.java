package com.automate.vehicleservices.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Chandrashekar V
 */
@Getter
@Setter
public class AdditionalServiceRequest {

    private int serviceTypeId;
    private List<Integer> serviceItems;

}
