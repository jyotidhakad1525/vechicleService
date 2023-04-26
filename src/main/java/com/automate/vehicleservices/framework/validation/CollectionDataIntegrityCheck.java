package com.automate.vehicleservices.framework.validation;

import jdk.jfr.Experimental;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Experimental
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CollectionDataIntegrityConstraintValidator.class)
public @interface CollectionDataIntegrityCheck {
    String message() default "Validation error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    DataIntegrityCheck value();
}

