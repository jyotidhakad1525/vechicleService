package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.ServiceEstimate;
import com.automate.vehicleservices.entity.ServiceEstimateResponse;
import com.automate.vehicleservices.repository.dtoprojection.RateCardItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Chandrashekar V
 */
public final class ServiceEstimateResponseBuilder {
    private double additionalServicesTotal;
    private String approvedBy;
    private String createdBy;
    private LocalDateTime createdDate;
    private Map<String, List<RateCardItem>> estimation;
    private byte estimationSentToCustomer;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
    private double total;
    //bi-directional many-to-one association to ServiceEstimateRequest
    private ServiceEstimate serviceEstimate;

    private ServiceEstimateResponseBuilder() {
    }

    public static ServiceEstimateResponseBuilder aServiceEstimateResponse() {
        return new ServiceEstimateResponseBuilder();
    }

    public ServiceEstimateResponseBuilder withAdditionalServicesTotal(double additionalServicesTotal) {
        this.additionalServicesTotal = additionalServicesTotal;
        return this;
    }

    public ServiceEstimateResponseBuilder withApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
        return this;
    }

    public ServiceEstimateResponseBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public ServiceEstimateResponseBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public ServiceEstimateResponseBuilder withEstimation(Map<String, List<RateCardItem>> estimation) {
        this.estimation = estimation;
        return this;
    }

    public ServiceEstimateResponseBuilder withEstimationSentToCustomer(byte estimationSentToCustomer) {
        this.estimationSentToCustomer = estimationSentToCustomer;
        return this;
    }

    public ServiceEstimateResponseBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public ServiceEstimateResponseBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public ServiceEstimateResponseBuilder withTotal(double total) {
        this.total = total;
        return this;
    }

    public ServiceEstimateResponseBuilder withServiceEstimate(ServiceEstimate serviceEstimate) {
        this.serviceEstimate = serviceEstimate;
        return this;
    }

    public ServiceEstimateResponseBuilder but() {
        return aServiceEstimateResponse().withAdditionalServicesTotal(additionalServicesTotal)
                .withApprovedBy(approvedBy).withCreatedBy(createdBy).withCreatedDate(createdDate)
                .withEstimation(estimation).withEstimationSentToCustomer(estimationSentToCustomer)
                .withLastModifiedBy(lastModifiedBy).withLastModifiedDate(lastModifiedDate).withTotal(total)
                .withServiceEstimate(serviceEstimate);
    }

    public ServiceEstimateResponse build() {
        ServiceEstimateResponse serviceEstimateResponse = new ServiceEstimateResponse();
        serviceEstimateResponse.setAdditionalServicesTotal(additionalServicesTotal);
        serviceEstimateResponse.setApprovedBy(approvedBy);
        serviceEstimateResponse.setCreatedBy(createdBy);
        serviceEstimateResponse.setCreatedDate(createdDate);
        serviceEstimateResponse.setEstimation(estimation);
        serviceEstimateResponse.setEstimationSentToCustomer(estimationSentToCustomer);
        serviceEstimateResponse.setLastModifiedBy(lastModifiedBy);
        serviceEstimateResponse.setLastModifiedDate(lastModifiedDate);
        serviceEstimateResponse.setTotal(total);
        serviceEstimateResponse.setServiceEstimate(serviceEstimate);
        return serviceEstimateResponse;
    }
}
