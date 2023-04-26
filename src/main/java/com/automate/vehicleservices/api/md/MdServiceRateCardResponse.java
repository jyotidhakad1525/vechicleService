package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.entity.MdServiceRateCard;
import com.automate.vehicleservices.framework.api.MdResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class MdServiceRateCardResponse implements MdResponse {

    private int id;
    private boolean applicableForDiesel;
    private boolean applicableForElectric;
    private boolean applicableForHybrid;
    private boolean applicableForPetrol;
    private double rate;
    private String vehicleModel;
    private int mdMaintenanceTypeId;
    private String mdMaintenanceType;
    private int mdServiceItemId;
    private String mdServiceItem;
    private int mdServiceTypeId;
    private String mdServiceType;
    private int mdTenant;


    public MdServiceRateCardResponse(MdServiceRateCard mdServiceRateCard) {
        if (Objects.isNull(mdServiceRateCard))
            return;

        this.id = mdServiceRateCard.getId();
        this.applicableForDiesel = mdServiceRateCard.getApplicableForDiesel();
        this.applicableForElectric = mdServiceRateCard.getApplicableForElectric();
        this.applicableForHybrid = mdServiceRateCard.getApplicableForHybrid();
        this.applicableForPetrol = mdServiceRateCard.getApplicableForPetrol();
        this.rate = mdServiceRateCard.getRate();
        this.vehicleModel = mdServiceRateCard.getVehicleModel();
        final var mdMaintenanceType = mdServiceRateCard.getMdMaintenanceType();
        if (Objects.nonNull(mdMaintenanceType)) {
            this.mdMaintenanceTypeId = mdMaintenanceType.getId();
            this.mdMaintenanceType = mdMaintenanceType.getType();
        }
        final var mdServiceItem = mdServiceRateCard.getMdServiceItem();
        if (Objects.nonNull(mdServiceItem)) {
            this.mdServiceItemId = mdServiceItem.getId();
            this.mdServiceItem = mdServiceItem.getName();
        }
        final var mdServiceType = mdServiceRateCard.getMdServiceType();
        if (Objects.nonNull(mdServiceType)) {
            this.mdServiceTypeId = mdServiceType.getId();
            this.mdServiceType = mdServiceType.getServiceName();
        }
        this.mdTenant = mdServiceRateCard.getMdTenant().getId();
    }
}
