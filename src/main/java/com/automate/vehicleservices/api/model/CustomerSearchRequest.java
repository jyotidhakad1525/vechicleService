package com.automate.vehicleservices.api.model;

import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class CustomerSearchRequest {
    private String vehicleRegNumber;
    private String contactNumber;
    private String vin;
    private String chassis;
    private String customerName;
    private String customerId;
    private String engineNumber;
    private String policyNumber;
    private String jobCardNumber;

    public int getCustomerId() {
        if (StringUtils.isBlank(customerId))
            return 0;
        try {
            return Integer.parseInt(customerId);
        } catch (NumberFormatException e) {
            throw new VehicleServicesException(String.format("Invalid Customer Id %s:", customerId));
        }
    }

    @JsonIgnore
    public boolean isSingleFieldSearch() {
        return noOfNonEmptySearchFields() == 1;
    }

    @JsonIgnore
    public boolean isMultiFieldSearch() {
        return noOfNonEmptySearchFields() > 1;
    }

    @JsonIgnore
    public boolean atLeastOneSearchFieldExists() {
        return noOfNonEmptySearchFields() >= 1;
    }

    private int noOfNonEmptySearchFields() {
        int count = 0;
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                if (!StringUtils.equalsIgnoreCase("log", field.getName()))
                    count = incrementNotBlankCount(count, field.get(this));
            } catch (IllegalAccessException e) {
                log.error("Can't access field via reflection", e.getMessage());
            }
        }

        return count;
    }

    private int incrementNotBlankCount(int count, Object dataPoint) {
        if (Objects.isNull(dataPoint))
            return count;

        if (dataPoint instanceof String)
            count = incrementNotBlankCount(count, (String) dataPoint);
        else if (dataPoint instanceof Integer)
            count = incrementNotBlankCount(count, (Integer) dataPoint);

        return count;
    }

    private int incrementNotBlankCount(int count, String dataPoint) {
        return StringUtils.isBlank(dataPoint) ? count : count + 1;

    }

    private int incrementNotBlankCount(int count, Integer dataPoint) {
        return dataPoint > 0 ? count + 1 : count;
    }
}
