package com.automate.vehicleservices.service.dto;

import com.automate.vehicleservices.api.model.VehicleRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@NoArgsConstructor
public class VehicleBulkUploadDTO {

    public static final String REG_NUMBER_NOT_PROVIDED = "REG_NUMBER_NOT_PROVIDED";
    private List<VehicleRequest> vehicles = new ArrayList<>();
    private AtomicInteger totalRecords = new AtomicInteger(0);
    private AtomicInteger failedRecords = new AtomicInteger(0);
    private Map<String, List<ValidationError>> errorMap = new HashMap<>();


    public void addVehicle(VehicleRequest vehicleRequest) {
        vehicles.add(vehicleRequest);
        incrementTotalRecords();
    }

    public void addError(String regNumber, ValidationError validationError) {
        incrementFailedRecords();
        incrementTotalRecords();
        if (StringUtils.isBlank(regNumber))
            return;
        errorMap.computeIfAbsent(regNumber, k -> new ArrayList<>()).add(validationError);
    }

    public void incrementFailedRecords() {
        failedRecords.getAndIncrement();
    }

    public void incrementTotalRecords() {
        totalRecords.getAndIncrement();
    }

    public int getTotalRecords() {
        return totalRecords.get();
    }

    public void addErrors(String regNumber, List<ValidationError> validationError, int currentRow) {
        incrementFailedRecords();
        incrementTotalRecords();
        regNumber = StringUtils.isBlank(regNumber) ? StringUtils.EMPTY : regNumber;
        if (StringUtils.isNoneBlank(regNumber))
            errorMap.computeIfAbsent(regNumber, k -> new ArrayList<>()).addAll(validationError);
        else
            errorMap.computeIfAbsent(String.format("Row_%s%s", currentRow, "regNumber"), k -> new ArrayList<>())
                    .addAll(validationError);
    }

    public int getFailedRecords() {
        return failedRecords.get();
    }
}
