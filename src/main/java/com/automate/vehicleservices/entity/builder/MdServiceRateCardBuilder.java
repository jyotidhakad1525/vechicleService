package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.*;

public final class MdServiceRateCardBuilder {
    private boolean applicableForDiesel;
    private boolean applicableForElectric;
    private boolean applicableForHybrid;
    private boolean applicableForPetrol;
    private double rate;
    private String vehicleModel;
    //uni-directional many-to-one association to MdMaintenanceType
    private MdMaintenanceType mdMaintenanceType;
    //uni-directional many-to-one association to MdServiceItem
    private MdServiceItem mdServiceItem;
    //uni-directional many-to-one association to MdServiceType
    private MdServiceType mdServiceType;
    //uni-directional many-to-one association to MdServiceTypeCriteria
    private MdServiceTypeCriteria mdServiceTypeCriteria;
    //bi-directional many-to-one association to MdTenant
    private MdTenant mdTenant;

    private MdServiceRateCardBuilder() {
    }

    public static MdServiceRateCardBuilder aMdServiceRateCard() {
        return new MdServiceRateCardBuilder();
    }

    public MdServiceRateCardBuilder withApplicableForDiesel(boolean applicableForDiesel) {
        this.applicableForDiesel = applicableForDiesel;
        return this;
    }

    public MdServiceRateCardBuilder withApplicableForElectric(boolean applicableForElectric) {
        this.applicableForElectric = applicableForElectric;
        return this;
    }

    public MdServiceRateCardBuilder withApplicableForHybrid(boolean applicableForHybrid) {
        this.applicableForHybrid = applicableForHybrid;
        return this;
    }

    public MdServiceRateCardBuilder withApplicableForPetrol(boolean applicableForPetrol) {
        this.applicableForPetrol = applicableForPetrol;
        return this;
    }

    public MdServiceRateCardBuilder withRate(double rate) {
        this.rate = rate;
        return this;
    }

    public MdServiceRateCardBuilder withVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
        return this;
    }

    public MdServiceRateCardBuilder withMdMaintenanceType(MdMaintenanceType mdMaintenanceType) {
        this.mdMaintenanceType = mdMaintenanceType;
        return this;
    }

    public MdServiceRateCardBuilder withMdServiceItem(MdServiceItem mdServiceItem) {
        this.mdServiceItem = mdServiceItem;
        return this;
    }

    public MdServiceRateCardBuilder withMdServiceType(MdServiceType mdServiceType) {
        this.mdServiceType = mdServiceType;
        return this;
    }

    public MdServiceRateCardBuilder withMdServiceTypeCriteria(MdServiceTypeCriteria mdServiceTypeCriteria) {
        this.mdServiceTypeCriteria = mdServiceTypeCriteria;
        return this;
    }

    public MdServiceRateCardBuilder withMdTenant(MdTenant mdTenant) {
        this.mdTenant = mdTenant;
        return this;
    }

    public MdServiceRateCardBuilder but() {
        return aMdServiceRateCard().withApplicableForDiesel(applicableForDiesel)
                .withApplicableForElectric(applicableForElectric).withApplicableForHybrid(applicableForHybrid)
                .withApplicableForPetrol(applicableForPetrol).withRate(rate).withVehicleModel(vehicleModel)
                .withMdMaintenanceType(mdMaintenanceType).withMdServiceItem(mdServiceItem)
                .withMdServiceType(mdServiceType).withMdServiceTypeCriteria(mdServiceTypeCriteria)
                .withMdTenant(mdTenant);
    }

    public MdServiceRateCard build() {
        MdServiceRateCard mdServiceRateCard = new MdServiceRateCard();
        mdServiceRateCard.setApplicableForDiesel(applicableForDiesel);
        mdServiceRateCard.setApplicableForElectric(applicableForElectric);
        mdServiceRateCard.setApplicableForHybrid(applicableForHybrid);
        mdServiceRateCard.setApplicableForPetrol(applicableForPetrol);
        mdServiceRateCard.setRate(rate);
        mdServiceRateCard.setVehicleModel(vehicleModel);
        mdServiceRateCard.setMdMaintenanceType(mdMaintenanceType);
        mdServiceRateCard.setMdServiceItem(mdServiceItem);
        mdServiceRateCard.setMdServiceType(mdServiceType);
        mdServiceRateCard.setMdServiceTypeCriteria(mdServiceTypeCriteria);
        mdServiceRateCard.setMdTenant(mdTenant);
        return mdServiceRateCard;
    }
}
