package com.automate.vehicleservices.service;

import com.automate.vehicleservices.service.dto.DataMap;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Chandrashekar V
 */
public class TupleSerializer extends StdSerializer<DataMap> {
    protected TupleSerializer(Class<DataMap> t) {
        super(t);
    }

    @Override
    public void serialize(DataMap dataMap, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

    }
}
