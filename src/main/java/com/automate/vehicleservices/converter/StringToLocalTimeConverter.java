package com.automate.vehicleservices.converter;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;

/**
 * @author Chandrashekar V
 */
public class StringToLocalTimeConverter implements Converter<JsonNode, LocalTime> {
    @Override
    public LocalTime convert(JsonNode s) {

        return LocalTime.of(s.findValue("hour").asInt(), s.findValue("minute").asInt(), s.findValue("second").asInt());
    }


}
