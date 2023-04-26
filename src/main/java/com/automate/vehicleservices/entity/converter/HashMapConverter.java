package com.automate.vehicleservices.entity.converter;

import com.automate.vehicleservices.service.ServiceEstimateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.Map;

/**
 * @author Chandrashekar V
 */
public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {
    private final Logger logger = LoggerFactory.getLogger(ServiceEstimateService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> map) {
        String json = StringUtils.EMPTY;
        try {
            json = objectMapper.writeValueAsString(map);
        } catch (final JsonProcessingException e) {
            logger.error("Exception in HashMapConverter. Unable to transform into JSON", e);
        }

        return json;
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String s) {
        Map<String, Object> map = null;
        try {
            map = objectMapper.readValue(s, Map.class);
        } catch (final IOException e) {
            logger.error("Exception in HashMapConverter. Unable to convert into Map from JSON", e);
        }

        return map;
    }
}
