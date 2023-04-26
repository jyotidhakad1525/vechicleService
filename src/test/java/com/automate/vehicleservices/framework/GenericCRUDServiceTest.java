package com.automate.vehicleservices.framework;

import com.automate.vehicleservices.entity.Customer;
import com.automate.vehicleservices.entity.builder.CustomerBuilder;
import com.automate.vehicleservices.framework.service.CrudService;
import com.automate.vehicleservices.repository.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenericCRUDServiceTest extends BaseTest {

    @Autowired
    CrudService crudService;

    @Test
    @Transactional
    void save() {
        Customer customer = CustomerBuilder.aCustomer().withFirstName("Chandra").withLastName("V")
                .withContactNumber("1231231231")
                .withEmail("chandra@gmail" +
                        ".com").build();
        Customer save = crudService.save(customer);
        assertEquals(customer.getFirstName(), save.getFirstName());
    }

    @Test
    @Transactional
    void saveAll() {

        final var contactNumber = "1231231231";
        Customer chandra = CustomerBuilder.aCustomer().withFirstName("Chandra").withLastName("V")
                .withContactNumber(contactNumber)
                .withEmail("chandra@gmail.com").build();

        Customer john = CustomerBuilder.aCustomer().withFirstName("John").withLastName("Summer")
                .withContactNumber("999999999")
                .withEmail("John@gmail.com").build();

        List<Customer> customers = List.of(chandra, john);
        Iterable<Customer> customerList = crudService.saveAll(customers);
        final var first = StreamSupport.stream(customerList.spliterator(), false)
                .filter(customer -> contactNumber.equalsIgnoreCase(customer.getContactNumber())).findFirst();
        assertTrue(first.isPresent());
    }

    @Test
    @Transactional
    void findById() {

        Customer customer = CustomerBuilder.aCustomer().withFirstName("Chandra").withLastName("V")
                .withContactNumber("1231231231")
                .withEmail("chandra@gmail" +
                        ".com").build();
        Customer save = crudService.save(customer);

        Optional<Customer> customerById = crudService.findById(save.getId(), Customer.class);

        assertEquals(customerById.get().getId(), save.getId());
    }

    @Test
    @Transactional
    void existsById() {
        Customer customer = CustomerBuilder.aCustomer().withFirstName("Chandra").withLastName("V")
                .withContactNumber("1231231231")
                .withEmail("chandra@gmail" +
                        ".com").build();
        Customer save = crudService.save(customer);

        boolean b = crudService.existsById(save.getId(), Customer.class);

        assertTrue(b);
    }

    @Test
    @Transactional
    void findAll() {

        final var contactNumber = "1231231231";
        Customer chandra = CustomerBuilder.aCustomer().withFirstName("Chandra").withLastName("V")
                .withContactNumber(contactNumber)
                .withEmail("chandra@gmail.com").build();

        Customer john = CustomerBuilder.aCustomer().withFirstName("John").withLastName("Summer")
                .withContactNumber("999999999")
                .withEmail("John@gmail.com").build();

        List<Customer> customers = List.of(chandra, john);
        Iterable<Customer> customerIterable = crudService.saveAll(customers);

        Iterable<Customer> all = crudService.findAll(Customer.class);

        final var first = StreamSupport.stream(all.spliterator(), false)
                .filter(customer -> contactNumber.equalsIgnoreCase(customer.getContactNumber())).findFirst();
        assertTrue(first.isPresent());
    }

    @Test
    @Transactional
    void findAllById() {

        final var contactNumber = "1231231231";
        Customer chandra = CustomerBuilder.aCustomer().withFirstName("Chandra").withLastName("V")
                .withContactNumber(contactNumber)
                .withEmail("chandra@gmail.com").build();

        Customer john = CustomerBuilder.aCustomer().withFirstName("John").withLastName("Summer")
                .withContactNumber("999999999")
                .withEmail("John@gmail.com").build();

        List<Customer> customers = List.of(chandra, john);
        Iterable<Customer> customerIterable = crudService.saveAll(customers);

        Iterable<Customer> all =
                crudService.findAllById(StreamSupport.stream(customerIterable.spliterator(), false).map(Customer::getId)
                                .collect(Collectors.toList())
                        , Customer.class);
        final var first = StreamSupport.stream(all.spliterator(), false)
                .filter(customer -> contactNumber.equalsIgnoreCase(customer.getContactNumber())).findFirst();
        assertTrue(first.isPresent());
    }

    @Test
    @Transactional
    void count() {

        Customer chandra = CustomerBuilder.aCustomer().withFirstName("Chandra").withLastName("V")
                .withContactNumber("1231231231")
                .withEmail("chandra@gmail.com").build();

        Customer john = CustomerBuilder.aCustomer().withFirstName("John").withLastName("Summer")
                .withContactNumber("999999999")
                .withEmail("John@gmail.com").build();

        List<Customer> customers = List.of(chandra, john);
        Iterable<Customer> customerIterable = crudService.saveAll(customers);
        long count = crudService.count(Customer.class);
        assertTrue(StreamSupport.stream(customerIterable.spliterator(), false).count() <= count);
    }

    @Test
    @Transactional
    void deleteById() {
        Customer chandra = CustomerBuilder.aCustomer().withFirstName("Chandra").withLastName("V")
                .withContactNumber("1231231231")
                .withEmail("chandra@gmail.com").build();

        Customer save = crudService.save(chandra);
        crudService.deleteById(save.getId(), Customer.class);

        Optional<Customer> byId = crudService.findById(save.getId(), Customer.class);
        assertTrue(!byId.isPresent());

    }

    @Test
    @Transactional
    void delete() {

        Customer chandra = CustomerBuilder.aCustomer().withFirstName("Chandra").withLastName("V")
                .withContactNumber("1231231231")
                .withEmail("chandra@gmail.com").build();

        Customer save = crudService.save(chandra);
        crudService.delete(save);

        Optional<Customer> byId = crudService.findById(save.getId(), Customer.class);
        assertTrue(!byId.isPresent());
    }

    @Test
    @Transactional
    void testDeleteAllEntities() {
        final var contactNumber = "1231231231";
        Customer chandra = CustomerBuilder.aCustomer().withFirstName("Chandra")
                .withLastName("V").withContactNumber(contactNumber)
                .withEmail("chandra@gmail.com").build();

        Customer john = CustomerBuilder.aCustomer().withFirstName("John").withLastName("Summer")
                .withContactNumber("999999999")
                .withEmail("John@gmail.com").build();

        List<Customer> customers = List.of(chandra, john);
        Iterable<Customer> customerIterable = crudService.saveAll(customers);
        List<Integer> listOfIds = StreamSupport.stream(customerIterable.spliterator(),
                false).map(Customer::getId).collect(Collectors.toList());
        crudService.deleteAll(customerIterable);

        Iterable<Customer> all =
                crudService.findAllById(listOfIds, Customer.class);
        final var first = StreamSupport.stream(all.spliterator(), false)
                .filter(customer -> contactNumber.equalsIgnoreCase(customer.getContactNumber())).findFirst();
        assertTrue(!first.isPresent());
    }
}