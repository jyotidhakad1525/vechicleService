package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.AddressRequest;
import com.automate.vehicleservices.api.model.v1.AddressRequestV1;
import com.automate.vehicleservices.entity.Customer;
import com.automate.vehicleservices.entity.CustomerAddress;
import com.automate.vehicleservices.entity.builder.CustomerAddressBuilder;
import com.automate.vehicleservices.repository.CustomerAddressRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerAddressService {

    private final CustomerAddressRepository customerAddressRepository;

    public CustomerAddressService(CustomerAddressRepository customerAddressRepository) {
        this.customerAddressRepository = customerAddressRepository;
    }


    /**
     * Fetches if address entity for the given input address. If not creates a new one.
     *
     * @param customer
     * @param addressRequest
     * @return
     */
    public CustomerAddress fetchOrCreateAddressEntity(Customer customer, AddressRequest addressRequest) {
        List<CustomerAddress> customerAddresses = customer.getCustomerAddresses();
        Optional<CustomerAddress> customerAddressOptional = fetchExistingCustomerAddressEntity(addressRequest,
                customerAddresses);

        if (customerAddressOptional.isPresent())
            return customerAddressOptional.get();

        return createAddressEntity(addressRequest);


    }

    /**
     * Fetches existing entity that matches with given input address values.
     *
     * @param addressRequest
     * @param customerAddresses
     * @return
     */
    private Optional<CustomerAddress> fetchExistingCustomerAddressEntity(@NotNull AddressRequest addressRequest,
                                                                         List<CustomerAddress> customerAddresses) {

        if (Objects.isNull(customerAddresses) || customerAddresses.isEmpty())
            return Optional.empty();

        return customerAddresses.stream().filter(customerAddress ->
                StringUtils.equalsIgnoreCase(customerAddress.getAddress(), addressRequest.getAddress())
                        && StringUtils.equalsIgnoreCase(customerAddress.getCity(), addressRequest.getCity())
                        && StringUtils.equalsIgnoreCase(customerAddress.getState(), addressRequest.getState())
                        && StringUtils.equalsIgnoreCase(customerAddress.getPin(), addressRequest.getPin())
                        && (customerAddress.getAddressLabel() == addressRequest.getLabel())
                        && StringUtils.equalsIgnoreCase(customerAddress.getAddressLabelOtherName(),
                        addressRequest.getAddressLabelIfOther())).findAny();


    }

    /**
     * Creates address entity from address request.
     *
     * @param addressRequest
     * @return
     */
    public CustomerAddress createAddressEntity(@NotNull AddressRequest addressRequest) {

        return CustomerAddressBuilder.aCustomerAddress()
                .withAddress(addressRequest.getAddress())
                .withAddressLabel(addressRequest.getLabel())
                .withAddressLabelOtherName(addressRequest.getAddressLabelIfOther()).withPin(addressRequest.getPin())
                .withCity(addressRequest.getCity())
                .withState(addressRequest.getState())
                .withDistrict(addressRequest.getDistrict())
                .withArea(addressRequest.getArea())
                .withLongitude(addressRequest.getLongitude()).withLatitude(addressRequest.getLatitude()).build();
    }

    public CustomerAddress createAddressEntityV1(@NotNull AddressRequestV1 addressRequest,Customer customer) {

        return CustomerAddressBuilder.aCustomerAddress()
                .withPin(addressRequest.getPin())
                .withHouseNumber(addressRequest.getHouseNo())
                .withStreet(addressRequest.getStreet())
                .withVillageOrTown(addressRequest.getVillageOrTown())
                .withCity(addressRequest.getCity())
                .withMandalOrTahasil(addressRequest.getMandalOrTahasil())
                .withState(addressRequest.getState())
                .withDistrict(addressRequest.getDistrict())
                .withIsUrban(addressRequest.getIsUrban())
                .withId(addressRequest.getId())
                .withCustomer(customer)
                .build();
    }

    public void updateCustomerAddressEntity(CustomerAddress customerAddress, AddressRequest addressRequest) {
        if (Objects.isNull(customerAddress))
            return;

        customerAddress.setAddress(addressRequest.getAddress());
        customerAddress.setAddressLabel(addressRequest.getLabel());
        customerAddress.setAddressLabelOtherName(addressRequest.getAddressLabelIfOther());
        customerAddress.setPin(addressRequest.getPin());
        customerAddress.setCity(addressRequest.getCity());
        customerAddress.setState(addressRequest.getState());
        customerAddress.setDistrict(addressRequest.getDistrict());
        customerAddress.setArea(addressRequest.getArea());
        customerAddress.setLongitude(addressRequest.getLongitude());
        customerAddress.setLatitude(addressRequest.getLatitude());
    }
}
