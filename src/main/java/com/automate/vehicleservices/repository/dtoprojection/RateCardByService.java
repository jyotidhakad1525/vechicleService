package com.automate.vehicleservices.repository.dtoprojection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chandrashekar V
 */
public class RateCardByService {
    private String serviceName;

    private List<RateCardItem> rateCardItems;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<RateCardItem> getRateCardItems() {
        if (rateCardItems == null)
            rateCardItems = new ArrayList<>();
        return rateCardItems;
    }

    public void setRateCardItems(
            List<RateCardItem> rateCardItems) {
        this.rateCardItems = rateCardItems;
    }

    public void addRateCardItem(RateCardItem rateCardItem) {
        getRateCardItems().add(rateCardItem);
    }
}
