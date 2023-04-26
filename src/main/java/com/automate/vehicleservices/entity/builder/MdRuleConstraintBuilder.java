package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.MdRuleConstraint;
import com.automate.vehicleservices.entity.MdRuleConstraintField;
import com.automate.vehicleservices.entity.MdTenant;

/**
 * Chandrashekar V
 */
public final class MdRuleConstraintBuilder {
    private String data;
    private String description;
    private String expression;
    //bi-directional many-to-one association to MdTenant
    private MdTenant mdTenant;
    //uni-directional many-to-one association to MdRuleConstraintField
    private MdRuleConstraintField mdRuleConstraintField;

    private MdRuleConstraintBuilder() {
    }

    public static MdRuleConstraintBuilder aMdRuleConstraint() {
        return new MdRuleConstraintBuilder();
    }

    public MdRuleConstraintBuilder withData(String data) {
        this.data = data;
        return this;
    }

    public MdRuleConstraintBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public MdRuleConstraintBuilder withExpression(String expression) {
        this.expression = expression;
        return this;
    }

    public MdRuleConstraintBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public MdRuleConstraintBuilder withMdRuleConstraintField(MdRuleConstraintField mdRuleConstraintField) {
        this.mdRuleConstraintField = mdRuleConstraintField;
        return this;
    }

    public MdRuleConstraintBuilder but() {
        return aMdRuleConstraint().withData(data).withDescription(description).withExpression(expression)
                .withMdTenant(mdTenant).withMdRuleConstraintField(mdRuleConstraintField);
    }

    public MdRuleConstraint build() {
        MdRuleConstraint mdRuleConstraint = new MdRuleConstraint();
        mdRuleConstraint.setData(data);
        mdRuleConstraint.setDescription(description);
        mdRuleConstraint.setExpression(expression);
        mdRuleConstraint.setMdTenant(mdTenant);
        mdRuleConstraint.setMdRuleConstraintField(mdRuleConstraintField);
        return mdRuleConstraint;
    }
}
