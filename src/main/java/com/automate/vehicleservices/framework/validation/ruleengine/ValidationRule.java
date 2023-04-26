package com.automate.vehicleservices.framework.validation.ruleengine;


/**
 * Each rule defined extends this custom class. This is needed to capture the result of the rule execution.
 */
public interface ValidationRule {

    String name();

    boolean condition(Facts facts);

    <T> T action(Facts facts);
}

