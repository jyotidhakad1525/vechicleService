package com.automate.vehicleservices.service;

import com.automate.vehicleservices.entity.Customer;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.repository.CustomerRepository;
import com.automate.vehicleservices.repository.dtoprojection.ServiceVehicleDTO;
import com.automate.vehicleservices.service.dto.CustomerDTO;
import com.automate.vehicleservices.service.dto.CustomerVehiclesDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Chandrashekar V
 */
@Service
public class CustomerService {

    private final PhoneNumberUtil phoneNumberUtil;

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository, PhoneNumberUtil phoneNumberUtil) {
        this.customerRepository = customerRepository;
        this.phoneNumberUtil = phoneNumberUtil;
    }

    @Transactional
    public List<Customer> findByContactNumber(final String contactNumber) {
        final var cleansedPhoneNumber = phoneNumberUtil.cleansePhoneNumber(contactNumber);
        return customerRepository.findByContactNumberIn(contactNumber, cleansedPhoneNumber);
    }


    @Transactional
    public List<CustomerDTO> fetchCustomerDTOByContactNumber(final String contactNumber) {
        final var customers = findByContactNumber(contactNumber);
        if (CollectionUtils.isEmpty(customers))
            return Collections.emptyList();
        return customers.stream().map(CustomerDTO::new).collect(Collectors.toList());
    }


    @Transactional
    public Boolean isContactNumberAlreadyMapped(String contactNumber) {
        final var cleansedPhoneNumber = phoneNumberUtil.cleansePhoneNumber(contactNumber);
        return customerRepository.existsByContactNumberIn(contactNumber, cleansedPhoneNumber);
    }

    @Transactional
    public CustomerVehiclesDTO fetchVehiclesByCustomer(int id) {

        final Optional<Customer> byId = getCustomer(id);

        if (byId.isEmpty())
            throw new VehicleServicesException("Customer not found");

        final var customer = byId.get();
        final var customerVehicles = customer.getCustomerVehicles();

        if (CollectionUtils.isEmpty(customerVehicles))
            return new CustomerVehiclesDTO(new CustomerDTO(customer), Collections.emptyList());

        final var vehicleDTOList = customerVehicles.stream().map(ServiceVehicleDTO::new).collect(Collectors.toList());
        return new CustomerVehiclesDTO(new CustomerDTO(customer), vehicleDTOList);
    }

    @Transactional
    public CustomerDTO findById(int id) {
        final Optional<Customer> customer = getCustomer(id);
        if (customer.isEmpty())
            throw new VehicleServicesException(HttpStatus.NOT_FOUND, "Customer not found");

        return new CustomerDTO(customer.get());
    }

    @Transactional
    public Optional<Customer> getCustomer(int id) {
        return customerRepository.findById(id);
    }

    @Transactional
    public Customer getCustomerOrThrowExceptionIfNotExists(int id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new VehicleServicesException(String.format("Customer not found for the given id : " +
                        "%d", id)));
    }
}
