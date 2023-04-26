package com.automate.vehicleservices.framework.validation.ruleengine;


import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registers facts for rule execution.
 */
public class Facts implements Iterable<Fact<?>> {

    private final Map<String, Fact<?>> registry = new ConcurrentHashMap<>();

    public <T> void register(String name, T t) {
        registry.put(name, new Fact<>(name, t));
    }

    public <T> T fetch(final String name) {
        Fact<?> fact = registry.get(name);
        if (Objects.nonNull(fact))
            return (T) fact.getT();

        return null;
    }

    @Override
    public Iterator<Fact<?>> iterator() {
        return registry.values().iterator();
    }
}
