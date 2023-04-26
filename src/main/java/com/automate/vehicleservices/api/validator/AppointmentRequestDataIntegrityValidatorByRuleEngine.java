package com.automate.vehicleservices.api.validator;

import com.automate.vehicleservices.api.model.AppointmentRequest;
import com.automate.vehicleservices.framework.validation.AbstractDataIntegrityValidatorByRuleEngine;
import com.automate.vehicleservices.framework.validation.ValidationRequest;
import com.automate.vehicleservices.framework.validation.ruleengine.Facts;
import com.automate.vehicleservices.framework.validation.ruleengine.ValidationRule;
import com.automate.vehicleservices.framework.validation.ruleengine.annotation.Rule;
import com.automate.vehicleservices.service.dto.ValidationError;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * Appointment request data integrity validator.
 */
public class AppointmentRequestDataIntegrityValidatorByRuleEngine extends AbstractDataIntegrityValidatorByRuleEngine {

    private static final String APPOINTMENT_REQUEST = "appointmentRequest";

    /**
     * Does basic validations such as : if pickup requested , then pick up address must be available, and, same goes for
     * drop.
     *
     * @param validationRequest
     * @return
     */
    @Override
    protected void registerFacts(ValidationRequest validationRequest, Facts facts) {
        facts.register(APPOINTMENT_REQUEST, validationRequest);
    }

    @Rule
    public static class pickupAddressRule implements ValidationRule {
        @Override
        public String name() {
            return this.getClass().getName();
        }

        @Override
        public boolean condition(Facts facts) {
            AppointmentRequest appointmentRequest = facts.fetch(APPOINTMENT_REQUEST);
            return appointmentRequest.isPickupRequired() && (Objects.isNull(appointmentRequest.getPickupAddress())
                    || StringUtils.isBlank(appointmentRequest.getPickupAddress().getAddress()));
        }

        @Override
        public <T> T action(Facts facts) {
            return (T) ValidationError.builder()
                    .error("Pickup address can not be empty when pickup is requested")
                    .field("pickupAddress").build();
        }

    }


    @Rule
    public static class dropAddressRule implements ValidationRule {

        @Override
        public String name() {
            return this.getClass().getName();
        }

        @Override
        public boolean condition(Facts facts) {
            AppointmentRequest appointmentRequest = facts.fetch(APPOINTMENT_REQUEST);
            return appointmentRequest.isDropRequired() && (Objects.isNull(appointmentRequest.getDropAddress())
                    || StringUtils.isBlank(appointmentRequest.getDropAddress().getAddress()));
        }

        @Override
        public <T> T action(Facts facts) {
            return (T) ValidationError.builder()
                    .error("Drop address can not be empty when drop is requested")
                    .field("dropAddress").build();
        }
    }


    @Rule
    public static class pickupTimeRule implements ValidationRule {

        @Override
        public String name() {
            return this.getClass().getName();
        }

        @Override
        public boolean condition(Facts facts) {
            AppointmentRequest appointmentRequest = facts.fetch(APPOINTMENT_REQUEST);
            return appointmentRequest.isPickupRequired() && Objects.isNull(appointmentRequest.getPickupTime());
        }

        @Override
        public <T> T action(Facts facts) {
            return (T) ValidationError.builder()
                    .error("Pickup Time can not be empty when pickup is requested")
                    .field("pickupTime").build();
        }
    }

    @Rule
    public static class dropTimeRule implements ValidationRule {
        @Override
        public String name() {
            return this.getClass().getName();
        }

        @Override
        public boolean condition(Facts facts) {
            AppointmentRequest appointmentRequest = facts.fetch(APPOINTMENT_REQUEST);
            return appointmentRequest.isDropRequired() && Objects.isNull(appointmentRequest.getDropTime());
        }

        @Override
        public <T> T action(Facts facts) {
            return (T) ValidationError.builder()
                    .error("Drop time can not be empty when drop is requested")
                    .field("dropTime").build();
        }
    }

    @Rule
    public static class dropTimeMustBeAfterServiceTimeRule implements ValidationRule {

        @Override
        public String name() {
            return this.getClass().getName();
        }

        @Override
        public boolean condition(Facts facts) {
            AppointmentRequest appointmentRequest = facts.fetch(APPOINTMENT_REQUEST);
            return appointmentRequest.isDropRequired()
                    && Objects.nonNull(appointmentRequest.getDropTime())
                    && appointmentRequest.getDropTime().toLocalDate()
                    .isBefore(appointmentRequest.getServiceRequestDate());
        }

        @Override
        public <T> T action(Facts facts) {
            return (T) ValidationError.builder()
                    .error("Drop time must be later than service date")
                    .field("dropTime").build();
        }
    }

    @Rule
    public static class pickupTimeMustBeBeforeServiceTimeRule implements ValidationRule {

        @Override
        public String name() {
            return this.getClass().getName();
        }

        @Override
        public boolean condition(Facts facts) {
            AppointmentRequest appointmentRequest = facts.fetch(APPOINTMENT_REQUEST);
            return appointmentRequest.isPickupRequired()
                    && Objects.nonNull(appointmentRequest.getPickupTime())
                    && appointmentRequest.getPickupTime().toLocalDate()
                    .isAfter(appointmentRequest.getServiceRequestDate());
        }

        @Override
        public <T> T action(Facts facts) {
            return (T) ValidationError.builder()
                    .error("Pickup time must be earlier than service date")
                    .field("pickupTime").build();
        }
    }
}
