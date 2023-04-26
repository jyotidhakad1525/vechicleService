package com.automate.vehicleservices.framework.validation.ruleengine;

/**
 * Runtime exception thrown when exception occurs while executing Rules.
 */
public class RuleEngineException extends RuntimeException {

    public RuleEngineException() {
    }

    public RuleEngineException(String message) {
        super(message);
    }

    public RuleEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuleEngineException(Throwable cause) {
        super(cause);
    }

    public RuleEngineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
