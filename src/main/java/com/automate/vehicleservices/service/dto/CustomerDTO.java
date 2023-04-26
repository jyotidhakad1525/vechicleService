package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.entity.Customer;
import com.automate.vehicleservices.entity.enums.Gender;
import com.automate.vehicleservices.entity.enums.Salutation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerDTO {
    private int id;
    private String altContactNumber;
    private String communicationPreference;
    private String contactNumber;
    private LocalDate dateOfBirth;
    private String email;
    private String firstName;
    private Gender gender;
    private boolean isActive;
    private String lastName;
    private LocalDate doa;
    private String customerType;
    private List<CustomerAddressDTO> customerAddresses;
    private String occupation;
    private String refered_by;
    private MdLeadSourceDTO leadSource;

    private Salutation salutation;

    private String relationName;

    private Integer age;

    public CustomerDTO(Customer customer) {
        if (Objects.isNull(customer))
            return;

        this.id = customer.getId();
        this.altContactNumber = customer.getContactNumber();
        this.communicationPreference = customer.getCommunicationPreference();
        this.contactNumber = customer.getContactNumber();
        this.dateOfBirth = customer.getDateOfBirth();
        this.email = customer.getEmail();
        this.firstName = customer.getFirstName();
        this.gender = customer.getGender();
        this.isActive = Optional.ofNullable(customer.getActive()).orElse(Boolean.FALSE);
        this.lastName = customer.getLastName();
        this.doa = customer.getDateOfArrival();
        this.customerType = customer.getCustomerType();
        this.occupation = customer.getOccupation();
        this.refered_by = customer.getRefered_by();
        final var customerAddresses = customer.getCustomerAddresses();
        if (CollectionUtils.isNotEmpty(customerAddresses))
            this.customerAddresses =
                    customerAddresses.stream().map(CustomerAddressDTO::new).collect(Collectors.toList());

        this.leadSource = new MdLeadSourceDTO(customer.getLeadSource());
        this.salutation = customer.getSalutation();
        this.relationName = customer.getRelationName();
        this.age = customer.getAge();
    }
}
