package com.automate.vehicleservices.framework.validation;

import com.automate.vehicleservices.service.dto.ValidationResponse;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * All data integrity validators are pointed to this validator class.
 * Based on the runtime annotation value, the respective Validator will be invoked.
 */
@Slf4j
public class CollectionDataIntegrityConstraintValidator implements ConstraintValidator<CollectionDataIntegrityCheck,
        Collection<? extends ValidationRequest>> {

    private final List<DataIntegrityValidator> dataIntegrityValidator = new ArrayList<>();


    @Override
    public void initialize(CollectionDataIntegrityCheck constraintAnnotation) {
        DataIntegrityCheck dataIntegrityCheck = constraintAnnotation.value();
        try {
            Class<? extends DataIntegrityValidator>[] value = dataIntegrityCheck.classes();

            for (Class<? extends DataIntegrityValidator> aClass : value) {
                dataIntegrityValidator.add(aClass.getDeclaredConstructor().newInstance());
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("Validator  instantiation failed", e);
        }
    }

    @Override
    public boolean isValid(Collection<? extends ValidationRequest> validationRequests,
                           ConstraintValidatorContext constraintValidatorContext) {

        AtomicBoolean errorExists = new AtomicBoolean(false);
        validationRequests.forEach(validationRequest -> {
            dataIntegrityValidator.stream().map(validator -> validator.validate(validationRequest))
                    .filter(validationResponse -> !validationResponse.isSuccess())
                    .forEach(validate -> {
                        errorExists.set(true);
                        captureErrors(validate, constraintValidatorContext);
                    });
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