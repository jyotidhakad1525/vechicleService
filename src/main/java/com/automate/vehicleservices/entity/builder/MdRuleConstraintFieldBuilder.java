package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.MdRuleConstraintField;
import com.automate.vehicleservices.entity.MdTenant;

/**
 * Chandrashekar V
 */
public final class MdRuleConstraintFieldBuilder {
    private String fieldDataType;
    private String fieldName;
    //bi-directional many-to-one association to MdTenant
    private MdTenant mdTenant;

    private MdRuleConstraintFieldBuilder() {
    }

    public static MdRuleConstraintFieldBuilder aMdRuleConstraintField() {
        return new MdRuleConstraintFieldBuilder();
    }

    public MdRuleConstraintFieldBuilder withFieldDataType(String fieldDataType) {
        this.fieldDataType = fieldDataType;
        return this;
    }

    public MdRuleConstraintFieldBuilder withFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public MdRuleConstraintFieldBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public MdRuleConstraintFieldBuilder but() {
        return aMdRuleConstraintField().withFieldDataType(fieldDataType).withFieldName(fieldName)
                .withMdTenant(mdTenant);
    }

    public MdRuleConstraintField build() {
        MdRuleConstraintField mdRuleConstraintField = new MdRuleConstraintField();
        mdRuleConstraintField.setFieldDataType(fieldDataType);
        mdRuleConstraintField.setFieldName(fieldName);
        mdRuleConstraintField.setMdTenant(mdTenant);
        return mdRuleConstraintField;
    }
}
