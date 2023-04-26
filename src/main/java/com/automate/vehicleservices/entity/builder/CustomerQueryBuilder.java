package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.Customer;
import com.automate.vehicleservices.entity.CustomerQuery;
import com.automate.vehicleservices.entity.ServiceVehicle;
import com.automate.vehicleservices.entity.enums.CustomerQueryEnquiryDepartment;
import com.automate.vehicleservices.entity.enums.CustomerQueryEnquiryPurpose;
import com.automate.vehicleservices.entity.enums.CustomerQueryEnquiryStatus;

import java.time.LocalDateTime;

public final class CustomerQueryBuilder {
    private String id;
    private Customer customer;
    private String creAssigned;
    private String actionPlan;
    private String creRemarks;
    private String query;
    private ServiceVehicle serviceVehicle;
    private CustomerQueryEnquiryDepartment customerQueryEnquiryDepartment;
    private CustomerQueryEnquiryStatus customerQueryEnquiryStatus;
    private CustomerQueryEnquiryPurpose customerQueryEnquiryPurpose;
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private CustomerQueryBuilder() {
    }

    public static CustomerQueryBuilder aCustomerQuery() {
        return new CustomerQueryBuilder();
    }

    public CustomerQueryBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public CustomerQueryBuilder withCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public CustomerQueryBuilder withCreAssigned(String creAssigned) {
        this.creAssigned = creAssigned;
        return this;
    }

    public CustomerQueryBuilder withActionPlan(String actionPlan) {
        this.actionPlan = actionPlan;
        return this;
    }

    public CustomerQueryBuilder withCreRemarks(String creRemarks) {
        this.creRemarks = creRemarks;
        return this;
    }

    public CustomerQueryBuilder withQuery(String query) {
        this.query = query;
        return this;
    }

    public CustomerQueryBuilder withServiceVehicle(ServiceVehicle serviceVehicle) {
        this.serviceVehicle = serviceVehicle;
        return this;
    }

    public CustomerQueryBuilder withCustomerQueryEnquiryDepartment(
            CustomerQueryEnquiryDepartment customerQueryEnquiryDepartment) {
        this.customerQueryEnquiryDepartment = customerQueryEnquiryDepartment;
        return this;
    }

    public CustomerQueryBuilder withCustomerQueryEnquiryStatus(CustomerQueryEnquiryStatus customerQueryEnquiryStatus) {
        this.customerQueryEnquiryStatus = customerQueryEnquiryStatus;
        return this;
    }

    public CustomerQueryBuilder withCustomerQueryEnquiryPurpose(
            CustomerQueryEnquiryPurpose customerQueryEnquiryPurpose) {
        this.customerQueryEnquiryPurpose = customerQueryEnquiryPurpose;
        return this;
    }

    public CustomerQueryBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public CustomerQueryBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public CustomerQueryBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public CustomerQueryBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public CustomerQuery build() {
        CustomerQuery customerQuery = new CustomerQuery();
        customerQuery.setId(id);
        customerQuery.setCustomer(customer);
        customerQuery.setAssignedTo(creAssigned);
        customerQuery.setActionPlan(actionPlan);
        customerQuery.setCreRemarks(creRemarks);
        customerQuery.setQuery(query);
        customerQuery.setServiceVehicle(serviceVehicle);
        customerQuery.setCustomerQueryEnquiryDepartment(customerQueryEnquiryDepartment);
        customerQuery.setCustomerQueryEnquiryStatus(customerQueryEnquiryStatus);
        customerQuery.setCustomerQueryEnquiryPurpose(customerQueryEnquiryPurpose);
        customerQuery.setCreatedBy(createdBy);
        customerQuery.setCreatedDate(createdDate);
        customerQuery.setLastModifiedBy(lastModifiedBy);
        customerQuery.setLastModifiedDate(lastModifiedDate);
        return customerQuery;
    }
}
