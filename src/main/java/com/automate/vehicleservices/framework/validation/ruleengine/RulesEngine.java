package com.automate.vehicleservices.framework.validation.ruleengine;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Parameter;
import java.util.*;

@Slf4j
public final class RulesEngine {

    public <T> Map<String, T> fireRules(Rules rules, Facts facts) {
        Map<String, T> result = new HashMap<>();
        for (ValidationRule rule : rules) {
            Object o = executeEachRule(facts, rule);
            if (Objects.nonNull(o)) {
                T t = (T) o;
                result.put(rule.name(), t);
            }
        }
        return result;
    }

    public <T> T executeEachRule(Facts facts, ValidationRule rule) {
       /* try {
            Method condition = rule.getClass().getMethod("condition", Fact.class);
            Parameter[] parameters = condition.getParameters();
            List<Fact> params = facts(facts, parameters);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }*/
        return conditionAndAction(rule, facts);
    }

    public <T> T conditionAndAction(ValidationRule rule, Facts facts) {
        try {
            if (rule.condition(facts))
                return rule.action(facts);
        } catch (Exception ex) {
            log.error(String.format("Rule execution failed. RuleName: %s", rule.name()), ex);
        }
        return null;

    }

    public List<Fact> facts(Facts facts, Parameter[] parameters) {
        List<Fact> params = new ArrayList<>();
        for (Parameter parameter : parameters) {
            com.automate.vehicleservices.framework.validation.ruleengine.annotation.Fact annotation = parameter.
                    getAnnotation(com.automate.vehicleservices.framework.validation.ruleengine.annotation.Fact.class);
            if (Objects.nonNull(annotation)) {
                params.add(facts.fetch(annotation.value()));
            } else throw new RuleEngineException("A non Fact objects is passed");
        }
        return params;
    }
}
