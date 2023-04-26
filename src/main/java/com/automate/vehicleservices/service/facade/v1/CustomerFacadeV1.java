package com.automate.vehicleservices.service.facade.v1;

import com.automate.vehicleservices.api.model.UpdateCustomerRequest;
import com.automate.vehicleservices.api.model.v1.CustomerRequestV1;
import com.automate.vehicleservices.entity.Customer;
import com.automate.vehicleservices.entity.CustomerAddress;
import com.automate.vehicleservices.entity.CustomerTenant;
import com.automate.vehicleservices.entity.MdLeadSource;
import com.automate.vehicleservices.entity.MdTenant;
import com.automate.vehicleservices.entity.builder.CustomerBuilder;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.framework.service.CrudService;
import com.automate.vehicleservices.service.CustomerAddressService;
import com.automate.vehicleservices.service.CustomerService;
import com.automate.vehicleservices.service.MdTenantService;
import com.automate.vehicleservices.service.dto.CustomerDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Chandrashekar V
 */
@Component
public class CustomerFacadeV1 {

    private final CustomerService customerService;

    private final MdTenantService tenantService;

    private final CrudService crudService;
    private final CustomerAddressService customerAddressService;

    public CustomerFacadeV1(CustomerService customerService, MdTenantService tenantService, CrudService crudService,
                            CustomerAddressService customerAddressService) {
        this.customerService = customerService;
        this.tenantService = tenantService;
        this.crudService = crudService;
        this.customerAddressService = customerAddressService;
    }

    /**
     * Fetches customer if existing, else creates new one.
     *
     * @param customerRequest
     * @param tenant
     * @return
     */
    public Customer fetchOrConstructCustomerEntity(CustomerRequestV1 customerRequest, MdTenant tenant) {
        List<Customer> customers = customerService.findByContactNumber(customerRequest.getContactNumber());

        if (CollectionUtils.isEmpty(customers)) {
            // create new customer entity
            return customerEntityFromCustomerRequest(customerRequest, tenant);
        }
        if (customers.size() > 1)
            throw new VehicleServicesException(String.format("More than one customer returned for the given contact " +
                    "number: %s", customerRequest.getContactNumber()));

        return customers.get(0);
    }

    /**
     * Verifies and returns if any customer exists with given contact number, if not creates a new entity and returns.
     *
     * @param customerRequest
     * @param mdTenant
     * @return
     */
    public Customer customerEntityFromCustomerRequest(@NotNull CustomerRequestV1 customerRequest,
                                                      final MdTenant mdTenant) {

        if (StringUtils.isBlank(customerRequest.getContactNumber()))
            throw new VehicleServicesException("Contact number is required to proceed with estimation request.");

        Customer customer = customerEntity(customerRequest);

        customer.addCustomerTenant(CustomerTenant.builder().mdTenant(mdTenant).customer(customer).build());
        return customer;
    }

    public Customer customerEntity(@NotNull CustomerRequestV1 customerRequest) {
        final CustomerBuilder customerBuilder = getCustomerBuilder(customerRequest);
        final var customer =  customerBuilder.build();

        final var addresses = customerRequest.getAddresses();
        if (!CollectionUtils.isEmpty(addresses)) {
            final var customerAddresses =
                    addresses.stream().map(entry -> customerAddressService.createAddressEntityV1(entry,customer)).collect(Collectors.toList());
            customer.setCustomerAddresses(customerAddresses);
        }
        return customer;

    }

    private CustomerBuilder getCustomerBuilder(@NotNull CustomerRequestV1 customerRequest) {
        final var leadSource = crudService.findById(customerRequest.getLeadSource(), MdLeadSource.class);
        return CustomerBuilder.aCustomer()
                .withSalutation(customerRequest.getSalutation())
                .withFirstName(customerRequest.getFirstName())
                .withLastName(customerRequest.getLastName())
                .withGender(customerRequest.getGender())
                .withRelationName(customerRequest.getRelationName())
                .withDateOfBirth(customerRequest.getDateOfBirth())
                .withAge(customerRequest.getAge())
                .withDateOfArrival(customerRequest.getDateOfArrival())
                .withContactNumber(customerRequest
                        .getContactNumber()).withEmail(customerRequest.getEmail())
                .withAltContactNumber(customerRequest.getAlternateContactNumber())
                .withEmail(customerRequest.getEmail())
                .withOccupation(customerRequest.getOccupation())
                .withLeadSource(leadSource.orElse(null))
                .withCustomerType(customerRequest.getCustomerType())
                .withActive(true);
    }


    /**
     * Verifies and returns if any customer exists with given contact number, if not creates a new entity and returns.
     *
     * @param customerRequest
     * @param mdTenant
     * @return
     */
    public CustomerDTO saveCustomer(@NotNull CustomerRequestV1 customerRequest,
                                    final String mdTenant) {
        List<Customer> customers = customerService.findByContactNumber(customerRequest.getContactNumber());
        if (CollectionUtils.isNotEmpty(customers))
            throw new VehicleServicesException("Another customer exists with same contact number.");

        final var customer = customerEntityFromCustomerRequest(customerRequest,
                tenantService.tenantByIdentifier(mdTenant));
        final var save = crudService.save(customer);
        return new CustomerDTO(save);
    }

    @Transactional
    public CustomerDTO updateCustomer(int customerId, UpdateCustomerRequest customerRequest, String tenant) {

        final var customerOpt = customerService.getCustomer(customerId);
        if (customerOpt.isEmpty())
            throw new VehicleServicesException("Customer doesn't exist to update.");

        final var leadSource = crudService.findById(customerRequest.getLeadSource(), MdLeadSource.class);

        Customer customer = customerOpt.get();
        customer.setContactNumber(customerRequest
                .getContactNumber());
        customer.setEmail(customerRequest.getEmail());
        customer.setFirstName(customerRequest.getFirstName());
        customer.setLastName(customerRequest.getLastName());
        customer.setAltContactNumber(customerRequest.getAlternateContactNumber());
        customer.setGender(customerRequest.getGender());
        customer.setDateOfArrival(customerRequest.getDateOfArrival());
        customer.setDateOfBirth(customerRequest.getDateOfBirth());
        customer.setCustomerType(customerRequest.getCustomerType());
        customer.setOccupation(customerRequest.getOccupation());
        customer.setRefered_by(customerRequest.getRefered_by());
        customer.setLeadSource(leadSource.orElse(null));

        var customerAddresses = customer.getCustomerAddresses();
        Map<Integer, CustomerAddress> customerAddressMap = null;
        if (CollectionUtils.isNotEmpty(customerAddresses)) {
            customerAddressMap = customerAddresses.stream().collect(Collectors.toMap(CustomerAddress::getId,
                    Function.identity()));
        }

        final Map<Integer, CustomerAddress> existingMappedAddressesMap = customerAddressMap;
        final var addresses = customerRequest.getAddresses();
        if (MapUtils.isNotEmpty(addresses))
            addresses.entrySet()
                    .forEach(entry -> {
                        if (existingMappedAddressesMap.containsKey(entry.getKey())) {
                            customerAddressService.updateCustomerAddressEntity(existingMappedAddressesMap.get(entry.getKey()),
                                    entry.getValue());
                        } else {
                            final var addressEntity = customerAddressService.createAddressEntity(entry.getValue());
                            customer.addCustomerAddress(addressEntity);
                        }
                    });

        final var save = crudService.save(customer);
        return new CustomerDTO(save);
    }


    public Customer getUpdateCustomerEntity(CustomerRequestV1 customerRequest, MdTenant tenant) {
        final var customerOpt = customerService.getCustomer(customerRequest.getId());
        if (customerOpt.isEmpty())
            throw new VehicleServicesException("Customer doesn't exist to update.");

        final var leadSource = crudService.findById(customerRequest.getLeadSource(), MdLeadSource.class);
        Customer customer = customerOpt.get();

        customer.setSalutation(customerRequest.getSalutation());
        customer.setFirstName(customerRequest.getFirstName());
        customer.setLastName(customerRequest.getLastName());
        customer.setGender(customerRequest.getGender());
        customer.setRelationName(customerRequest.getRelationName());
        customer.setDateOfBirth(customerRequest.getDateOfBirth());
        customer.setAge(customerRequest.getAge());
        customer.setDateOfArrival(customerRequest.getDateOfArrival());
        customer.setContactNumber(customerRequest
                .getContactNumber());
        customer.setAltContactNumber(customerRequest.getAlternateContactNumber());
        customer.setEmail(customerRequest.getEmail());
        customer.setOccupation(customerRequest.getOccupation());
        customer.setLeadSource(leadSource.orElse(null));
        customer.setCustomerType(customerRequest.getCustomerType());

        var addresses = customerRequest.getAddresses();
        if (!CollectionUtils.isEmpty(addresses)) {
            final var customerAddresses =
                    addresses.stream().map(entry -> customerAddressService.createAddressEntityV1(entry,customer)).collect(Collectors.toList());
            customer.setCustomerAddresses(customerAddresses);
        }

        return customer;
    }
}
