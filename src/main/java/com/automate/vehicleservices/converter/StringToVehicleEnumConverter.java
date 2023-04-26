package com.automate.vehicleservices.converter;

import com.automate.vehicleservices.entity.enums.FuelType;
import org.springframework.core.convert.converter.Converter;

/**
 * @author Chandrashekar V
 */
public class StringToVehicleEnumConverter implements Converter<String, FuelType> {
    @Override
    public FuelType convert(String s) {
        return FuelType.valueOf(s.toUpperCase());
    }


}
