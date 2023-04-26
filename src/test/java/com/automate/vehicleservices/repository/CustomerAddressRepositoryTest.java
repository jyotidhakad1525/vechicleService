package com.automate.vehicleservices.repository;

import com.automate.vehicleservices.api.model.AddressLabel;
import com.automate.vehicleservices.entity.CustomerAddress;
import com.automate.vehicleservices.entity.builder.CustomerAddressBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerAddressRepositoryTest extends BaseTest {

    @Autowired
    private CustomerAddressRepository customerAddressRepository;


    @Test
    @Transactional
    public void testSave() {
        CustomerAddress customerAddress = CustomerAddressBuilder.aCustomerAddress().withAddress("Road No.1, Banjarahills")
                .withAddressLabel(AddressLabel.HOME).withCity("Hyderabad").withState("TS").withPin("500034")
                .withLongitude(17.4347929f).withLatitude(78.386769f).build();

        CustomerAddress save = customerAddressRepository.save(customerAddress);
        assertNotNull(save);
    }

}