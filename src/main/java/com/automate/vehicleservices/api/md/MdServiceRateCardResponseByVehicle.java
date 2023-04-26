package com.automate.vehicleservices.api.md;

import com.automate.vehicleservices.entity.MdServiceRateCard;
import com.automate.vehicleservices.framework.api.MdResponse;
import lombok.Getter;

import java.util.*;

@Getter
public class MdServiceRateCardResponseByVehicle implements MdResponse {

    private final Map<String, Map<Integer, List<MdServiceRateCardResponse>>> byVehicleServiceType = new HashMap<>();


    public void addRateCardEntry(MdServiceRateCard mdServiceRateCard) {
        if (Objects.isNull(mdServiceRateCard))
            return;

        byVehicleServiceType.computeIfAbsent(mdServiceRateCard.getVehicleModel(), k -> new HashMap<>())
                .computeIfAbsent(mdServiceRateCard.getMdServiceType().getId(), k -> new ArrayList<>())
                .add(new MdServiceRateCardResponse(mdServiceRateCard));

    }
}
