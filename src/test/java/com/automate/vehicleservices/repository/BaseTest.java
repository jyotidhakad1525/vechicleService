package com.automate.vehicleservices.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;


/**
 * @author Chandrashekar V
 */
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
public abstract class BaseTest {

    @Autowired
    protected MdTenantRepository tenantRepository;


    // Per class tear down data, called after all test methods are done executing.
    private Map<String, Map<CrudRepository, List<Object>>> tearDownData = new HashMap<>();


    private void setUp(final String classname) {
        tearDownData.put(classname, new LinkedHashMap<>());
    }

    private void tearDown(final String className) {

        tearDownData.get(className).entrySet().stream().filter(e -> Objects.nonNull(e.getValue()))
                .forEach(entry -> entry.getValue().forEach(id -> entry.getKey().deleteById(id)));
    }

    protected String self() {
        return this.getClass().getName();
    }

    @BeforeAll
    void setUp() {
        setUp(self());
    }

    @AfterAll
    void tearDown() {
        tearDown(self());
    }

    Map<CrudRepository, List<Object>> tearDownData() {
        return tearDownData.get(self());
    }

    void addToTearDown(final CrudRepository crudRepository, final Object id) {
        tearDownData().computeIfAbsent(crudRepository, k -> new ArrayList<>()).add(id);
    }

    void addAllToTearDown(final CrudRepository crudRepository, final List<Object> id) {
        tearDownData().computeIfAbsent(crudRepository, k -> new ArrayList<>()).addAll(id);
    }

    public String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
