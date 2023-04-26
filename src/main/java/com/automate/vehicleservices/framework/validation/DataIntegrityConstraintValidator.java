package com.automate.vehicleservices.framework.validation;

import com.automate.vehicleservices.service.dto.ValidationResponse;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * All data integrity validators are pointed to this validator class. Based on the runtime annotation value, the
 * respective Validator will be invoked.
 */
@Slf4j
public class DataIntegrityConstraintValidator implements ConstraintValidator<DataIntegrityCheck, ValidationRequest> {

    private final List<DataIntegrityValidator> dataIntegrityValidator = new ArrayList<>();

    @Override
    public void initialize(DataIntegrityCheck constraintAnnotation) {
        try {
            Class<? extends DataIntegrityValidator>[] value = constraintAnnotation.classes();

            for (Class<? extends DataIntegrityValidator> aClass : value) {
                dataIntegrityValidator.add(aClass.getDeclaredConstructor().newInstance());
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("Validator  instantiation failed", e);
        }
    }

    @Override
    public boolean isValid(ValidationRequest t, ConstraintValidatorContext constraintValidatorContext) {
        AtomicBoolean errorExists = new AtomicBoolean(false);
        dataIntegrityValidator.stream().map(validator -> validator.validate(t))
                .filter(validationResponse -> !validationResponse.isSuccess())
                .forEach(validate -> {
                    errorExists.set(true);
                    captureErrors(validate, constraintValidatorContext);
                });

        return !errorExists.get();
    }

    public void captureErrors(ValidationResponse validationResponse,
                              ConstraintValidatorContext constraintValidatorContext) {
        if (!validationResponse.getErrors().isEmpty())
            validationResponse.getErrors().forEach(error -> constraintValidatorContext
                    .buildConstraintViolationWithTemplate(error.getError()).addPropertyNode(error.getField())
                    .addConstraintViolation());

    }


}