package com.automate.vehicleservices.framework.validation;

import com.automate.vehicleservices.api.model.builder.ValidationResponseBuilder;
import com.automate.vehicleservices.framework.validation.ruleengine.Facts;
import com.automate.vehicleservices.framework.validation.ruleengine.Rules;
import com.automate.vehicleservices.framework.validation.ruleengine.RulesEngine;
import com.automate.vehicleservices.framework.validation.ruleengine.ValidationRule;
import com.automate.vehicleservices.framework.validation.ruleengine.annotation.Rule;
import com.automate.vehicleservices.service.dto.ValidationError;
import com.automate.vehicleservices.service.dto.ValidationResponse;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;

/**
 * @author Chandrashekar V
 */
@Slf4j
public abstract class AbstractDataIntegrityValidatorByRuleEngine implements DataIntegrityValidator {

    private final RulesEngine rulesEngine = new RulesEngine();

    @Override
    public ValidationResponse validate(ValidationRequest validationRequest) {
        Facts facts = new Facts();
        registerFacts(validationRequest, facts);
        return executeRules(facts);
    }

    protected abstract void registerFacts(ValidationRequest validationRequest, Facts facts);

    public ValidationResponse executeRules(Facts facts) {
        ValidationResponseBuilder validationResponseBuilder = ValidationResponseBuilder.aValidationResponse();
        final Rules rules = registerRules();
        Map<String, Object> results = rulesEngine.fireRules(rules, facts);
        results.values().stream().filter(Objects::nonNull).filter(t -> t instanceof ValidationError)
                .map(t -> (ValidationError) t)
                .forEach(validationError -> validationResponseBuilder.withError(validationError));

        return validationResponseBuilder.build();
    }

    /**
     * Reads all inner classes from the concrete class that are annotated with <code>@Rule</code> and instantiate them.
     *
     * @return
     */
    public Rules registerRules() {
        Class<?>[] nestMembers = this.getClass().getNestMembers();
        Rules rules = new Rules();
        for (Class<?> nestMember : nestMembers) {
            try {
                if (null != nestMember.getAnnotation(Rule.class)) {
                    ValidationRule rule = (ValidationRule) nestMember.getDeclaredConstructor().newInstance();
                    rules.register(rule.name(), rule);
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error(String.format("Unable to instantiate rule class: %S", nestMember.getName()));
            }
        }
        return rules;
    }
}
