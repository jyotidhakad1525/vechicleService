package com.automate.vehicleservices.api.model.v1;


import com.automate.vehicleservices.api.model.VehicleInsuranceRequest;
import com.automate.vehicleservices.api.model.VehicleWarrantyRequest;
import com.automate.vehicleservices.api.validator.ServiceHistoryRequestDataIntegrityValidatorByRuleEngineV1;
import com.automate.vehicleservices.framework.validation.DataIntegrityCheck;
import com.automate.vehicleservices.framework.validation.ValidationRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DataIntegrityCheck(classes =
        ServiceHistoryRequestDataIntegrityValidatorByRuleEngineV1.class)
@Builder
public class VehicleServiceHistoryRequestV1 implements ValidationRequest {

    private VehicleHistoryRequest vehicleHistoryRequest;

    private List<VehicleWarrantyRequest> warrantyRequests;

    private VehicleInsuranceRequest insuranceRequest;

    @NotNull
    private VehicleDetailsV1 vehicleDetails;
    @NotNull
    private CustomerRequestV1 customer;
}
