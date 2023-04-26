package com.automate.vehicleservices.service.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Chandrashekar V
 * <p>
 * Dynamically map the data
 */
public class DynamicResponse {

    Map<String, Object> details = new LinkedHashMap<>();

    @JsonAnySetter
    void setDetails(String key, Object object) {
        details.put(key, object);
    }

    @JsonAnyGetter
    void getDetails(String key) {
        details.get(key);
    }
}
