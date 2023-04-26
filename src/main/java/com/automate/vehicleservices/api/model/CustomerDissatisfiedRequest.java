package com.automate.vehicleservices.api.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class CustomerDissatisfiedRequest {
    Map<String, Object> details = new LinkedHashMap<>();
    private boolean escalate;
    private String reasonForDissatisfaction;
    private String department;
    private String complaint;
    private String feedback;

    @SneakyThrows
    public static void main(String[] args) {
        CustomerDissatisfiedRequest customerDissatisfiedRequest = new CustomerDissatisfiedRequest();
        final Map details = new HashMap<Object, Object>();
        details.put("key1", "value1");
        details.put("key2", "value2");
        customerDissatisfiedRequest.details = details;
        System.out.println(new ObjectMapper().writeValueAsString(customerDissatisfiedRequest));
    }

    @JsonAnySetter
    void setDetail(String key, Object value) {
        details.put(key, value);
    }
}
