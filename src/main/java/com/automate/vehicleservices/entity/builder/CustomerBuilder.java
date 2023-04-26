package com.automate.vehicleservices.entity.builder;

import com.automate.vehicleservices.entity.*;
import com.automate.vehicleservices.entity.enums.Gender;
import com.automate.vehicleservices.entity.enums.Salutation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CustomerBuilder {
    private int id;
    private String altContactNumber;
    private String communicationPreference;
    private String contactNumber;
    private LocalDate dateOfBirth;
    private LocalDate dateOfArrival;
    private String email;
    private String customerType;
    private String occupation;
    private String refered_by;
    private String firstName;
    private Gender gender;
    private Boolean active;
    private String lastName;
    private List<CustomerAddress> customerAddresses = new ArrayList<>();
    private List<ServiceVehicle> customerVehicles = new ArrayList<>();
    private List<ServiceDocument> serviceDocuments = new ArrayList<>();
    private List<ServiceEstimate> serviceEstimates = new ArrayList<>();
    private List<ServiceAppointment> serviceAppointments = new ArrayList<>();
    private MdLeadSource leadSource;
    private Set<CustomerTenant> customerTenants = new HashSet<>();
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    private Salutation salutation;
    private String relationName;

    private Integer age;

    private CustomerBuilder() {
    }

    public static CustomerBuilder aCustomer() {
        return new CustomerBuilder();
    }

    public CustomerBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public CustomerBuilder withSalutation(Salutation salutation) {
        this.salutation = salutation;
        return this;
    }

    public CustomerBuilder withRelationName(String relationName) {
        this.relationName = relationName;
        return this;
    }

    public CustomerBuilder withAge(Integer age) {
        this.age = age;
        return this;
    }

    public CustomerBuilder withAltContactNumber(String altContactNumber) {
        this.altContactNumber = altContactNumber;
        return this;
    }

    public CustomerBuilder withCommunicationPreference(String communicationPreference) {
        this.communicationPreference = communicationPreference;
        return this;
    }

    public CustomerBuilder withContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
        return this;
    }

    public CustomerBuilder withDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public CustomerBuilder withDateOfArrival(LocalDate dateOfArrival) {
        this.dateOfArrival = dateOfArrival;
        return this;
    }

    public CustomerBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public CustomerBuilder withCustomerType(String customerType) {
        this.customerType = customerType;
        return this;
    }

    public CustomerBuilder withOccupation(String occupation) {
        this.occupation = occupation;
        return this;
    }
    public CustomerBuilder withReferedBy(String refered_by) {
        this.refered_by = refered_by;
        return this;
    }
   

    public CustomerBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public CustomerBuilder withGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public CustomerBuilder withActive(Boolean active) {
        this.active = active;
        return this;
    }

    public CustomerBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public CustomerBuilder withCustomerAddresses(List<CustomerAddress> customerAddresses) {
        this.customerAddresses = customerAddresses;
        return this;
    }

    public CustomerBuilder withCustomerVehicles(List<ServiceVehicle> customerVehicles) {
        this.customerVehicles = customerVehicles;
        return this;
    }

    public CustomerBuilder withServiceDocuments(List<ServiceDocument> serviceDocuments) {
        this.serviceDocuments = serviceDocuments;
        return this;
    }

    public CustomerBuilder withServiceEstimates(List<ServiceEstimate> serviceEstimates) {
        this.serviceEstimates = serviceEstimates;
        return this;
    }

    public CustomerBuilder withServiceAppointments(List<ServiceAppointment> serviceAppointments) {
        this.serviceAppointments = serviceAppointments;
        return this;
    }

    public CustomerBuilder withLeadSource(MdLeadSource leadSource) {
        this.leadSource = leadSource;
        return this;
    }

    public CustomerBuilder withCustomerTenants(Set<CustomerTenant> customerTenants) {
        this.customerTenants = customerTenants;
        return this;
    }

    public CustomerBuilder withCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public CustomerBuilder withCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public CustomerBuilder withLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public CustomerBuilder withLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public Customer build() {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setAltContactNumber(altContactNumber);
        customer.setCommunicationPreference(communicationPreference);
        customer.setContactNumber(contactNumber);
        customer.setDateOfBirth(dateOfBirth);
        customer.setDateOfArrival(dateOfArrival);
        customer.setEmail(email);
        customer.setCustomerType(customerType);
        customer.setOccupation(occupation);
        customer.setRefered_by(refered_by);
        customer.setFirstName(firstName);
        customer.setGender(gender);
        customer.setActive(active);
        customer.setLastName(lastName);
        customer.setCustomerAddresses(customerAddresses);
        customer.setCustomerVehicles(customerVehicles);
        customer.setServiceDocuments(serviceDocuments);
        customer.setServiceEstimates(serviceEstimates);
        customer.setServiceAppointments(serviceAppointments);
        customer.setLeadSource(leadSource);
        customer.setCustomerTenants(customerTenants);
        customer.setCreatedBy(createdBy);
        customer.setCreatedDate(createdDate);
        customer.setLastModifiedBy(lastModifiedBy);
        customer.setLastModifiedDate(lastModifiedDate);
        customer.setSalutation(salutation);
        customer.setRelationName(relationName);
        customer.setAge(age);
        return customer;
    }
}
