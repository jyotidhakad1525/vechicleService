package com.automate.vehicleservices.framework.validation.ruleengine;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Holds all the Rules.
 */
public class Rules implements Iterable<ValidationRule> {

    private final Map<String, ValidationRule> ruleMap = new ConcurrentHashMap<>();

    public void register(final String name, ValidationRule validationRule) {
        ruleMap.put(name, validationRule);
    }

    public ValidationRule fetch(final String name) {
        return ruleMap.get(name);
    }

    @Override
    public Iterator<ValidationRule> iterator() {
        return ruleMap.values().iterator();
    }
}
