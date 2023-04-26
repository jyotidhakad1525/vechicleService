package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.repository.dtoprojection.RateCardItem;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Chandrashekar V
 */
@JsonRootName("serviceRateCard")
@Getter
@Setter
@NoArgsConstructor
public class ServiceRateCardDTO {

    private String category;

    private int categoryId;

    private List<ServiceTypeData> serviceTypeData;

    public ServiceRateCardDTO(String category, int categoryId) {
        this.category = category;
        this.categoryId = categoryId;
        this.serviceTypeData = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceRateCardDTO that = (ServiceRateCardDTO) o;

        if (!getCategory().equals(that.getCategory())) return false;
        return getServiceTypeData() != null ? getServiceTypeData().equals(that.getServiceTypeData()) :
                that.getServiceTypeData() == null;
    }

    @Override
    public int hashCode() {
        int result = getCategory().hashCode();
        result = 31 * result + (getServiceTypeData() != null ? getServiceTypeData().hashCode() : 0);
        return result;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class ServiceTypeData {
        private String serviceName;
        private int serviceTypeId;
        private List<RateCardItem> rateCardItems = new ArrayList<>();


        public ServiceTypeData(String serviceName, final int serviceTypeId,
                               List<RateCardItem> rateCardItems) {
            this.serviceName = serviceName;
            this.serviceTypeId = serviceTypeId;
            this.rateCardItems = rateCardItems;
        }

        public ServiceTypeData(String serviceName, int serviceTypeId) {
            this.serviceName = serviceName;
            this.serviceTypeId = serviceTypeId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ServiceTypeData that = (ServiceTypeData) o;

            if (getServiceTypeId() != that.getServiceTypeId()) return false;
            return getServiceName() != null ? getServiceName().equals(that.getServiceName()) :
                    that.getServiceName() == null;
        }

        @Override
        public int hashCode() {
            int result = getServiceName() != null ? getServiceName().hashCode() : 0;
            result = 31 * result + getServiceTypeId();
            return result;
        }
    }


}
