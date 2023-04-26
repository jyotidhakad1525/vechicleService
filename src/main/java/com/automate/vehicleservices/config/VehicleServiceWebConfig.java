package com.automate.vehicleservices.config;

import com.automate.vehicleservices.converter.StringToLocalTimeConverter;
import com.automate.vehicleservices.converter.StringToVehicleEnumConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Chandrashekar V
 */
@Configuration
public class VehicleServiceWebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToVehicleEnumConverter());
        registry.addConverter(new StringToLocalTimeConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "PATCH")
                .allowedOrigins("*");
    }


}
