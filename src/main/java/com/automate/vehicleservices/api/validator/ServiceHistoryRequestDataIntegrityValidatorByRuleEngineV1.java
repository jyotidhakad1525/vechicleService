package com.automate.vehicleservices.api.validator;

import com.automate.vehicleservices.api.model.v1.VehicleServiceHistoryRequestV1;
import com.automate.vehicleservices.entity.enums.FuelType;
import com.automate.vehicleservices.framework.CustomBeanFactory;
import com.automate.vehicleservices.framework.validation.AbstractDataIntegrityValidatorByRuleEngine;
import com.automate.vehicleservices.framework.validation.ValidationRequest;
import com.automate.vehicleservices.framework.validation.ruleengine.Facts;
import com.automate.vehicleservices.framework.validation.ruleengine.ValidationRule;
import com.automate.vehicleservices.framework.validation.ruleengine.annotation.Rule;
import com.automate.vehicleservices.service.ServiceVehicleService;
import com.automate.vehicleservices.service.dto.ValidationError;

import java.util.Arrays;
import java.util.Objects;

public class ServiceHistoryRequestDataIntegrityValidatorByRuleEngineV1 extends AbstractDataIntegrityValidatorByRuleEngine {

    private static final String SERVICE_HISTORY_REQUEST = "serviceHistoryRequestV1";

    @Override
    protected void registerFacts(ValidationRequest validationRequest, Facts facts) {

        facts.register(SERVICE_HISTORY_REQUEST, validationRequest);
    }

    @Rule
    public static class newVehicleMustHaveRequiredFieldsProvided implements ValidationRule {
        @Override
        public String name() {
            return this.getClass().getName();
        }

        @Override
        public boolean condition(Facts facts) {
            final var vehicleServiceHistoryRequest =
                    (VehicleServiceHistoryRequestV1) facts.fetch(SERVICE_HISTORY_REQUEST);

            final var vehicleDetails = vehicleServiceHistoryRequest.getVehicleDetails();
            final var vehicleRegNumber =
                    vehicleDetails.getVehicleRegNumber();

            final var bean = CustomBeanFactory.bean(ServiceVehicleService.class);
            final var byVehicleRegNumber = bean.findByVehicleRegNumber(vehicleRegNumber);
            return Objects.isNull(byVehicleRegNumber) && Objects.isNull(vehicleDetails.getCurrentKmReading());
//                    && (StringUtils.isBlank(vehicleDetails.getVehicleModel())
//                    || Objects.isNull(vehicleDetails.getFuelType()) /*|| vehicleDetails
//                    .getKmReading() < vehicleServiceHistoryRequest.getKmReadingAtService()*/);
        }

        @Override
        public <T> T action(Facts facts) {
            final var vehicleDetails =
                    ((VehicleServiceHistoryRequestV1) facts.fetch(SERVICE_HISTORY_REQUEST)).getVehicleDetails();

            return (T) ValidationError.builder()
                    .error(String.format("Trying to add a new vehicle. Please provide valid Vehicle KM reading, " +
                            "Vehicle Model and Vehicle Variant %s", Arrays.toString(FuelType.values())))
                    .field("vehicleDetails").build();

        }

    }
}
