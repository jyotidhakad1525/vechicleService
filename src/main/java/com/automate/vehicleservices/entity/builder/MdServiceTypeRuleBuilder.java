package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.MdServiceType;
import com.automate.vehicleservices.entity.MdServiceTypeRule;
import com.automate.vehicleservices.entity.MdTenant;

/**
 * Chandrashekar V
 */
public final class MdServiceTypeRuleBuilder {
    private String description;
    private String ruleExpression;
    //bi-directional many-to-one association to MdTenant
    private MdTenant mdTenant;
    //uni-directional many-to-one association to MdServiceType
    private MdServiceType mdServiceType;

    private MdServiceTypeRuleBuilder() {
    }

    public static MdServiceTypeRuleBuilder aMdServiceTypeRule() {
        return new MdServiceTypeRuleBuilder();
    }

    public MdServiceTypeRuleBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public MdServiceTypeRuleBuilder withRuleExpression(String ruleExpression) {
        this.ruleExpression = ruleExpression;
        return this;
    }

    public MdServiceTypeRuleBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public MdServiceTypeRuleBuilder withMdServiceType(MdServiceType mdServiceType) {
        this.mdServiceType = mdServiceType;
        return this;
    }

    public MdServiceTypeRuleBuilder but() {
        return aMdServiceTypeRule().withDescription(description).withRuleExpression(ruleExpression)
                .withMdTenant(mdTenant).withMdServiceType(mdServiceType);
    }

    public MdServiceTypeRule build() {
        MdServiceTypeRule mdServiceTypeRule = new MdServiceTypeRule();
        mdServiceTypeRule.setDescription(description);
        mdServiceTypeRule.setRuleExpression(ruleExpression);
        mdServiceTypeRule.setMdTenant(mdTenant);
        mdServiceTypeRule.setMdServiceType(mdServiceType);
        return mdServiceTypeRule;
    }
}
