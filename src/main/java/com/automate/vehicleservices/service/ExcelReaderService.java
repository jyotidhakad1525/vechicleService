package com.automate.vehicleservices.service;

import com.automate.vehicleservices.api.model.CustomerRequest;
import com.automate.vehicleservices.api.model.VehicleDetails;
import com.automate.vehicleservices.api.model.VehicleRequest;
import com.automate.vehicleservices.entity.enums.FuelType;
import com.automate.vehicleservices.entity.enums.Gender;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import com.automate.vehicleservices.service.dto.ValidationError;
import com.automate.vehicleservices.service.dto.VehicleBulkUploadDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExcelReaderService {

    public static final String VEHICLE = "VEHICLE";
    public static final DataFormatter DATA_FORMATTER = new DataFormatter();
    public static final String MM_DD_YY = "MM/dd/yy";
    static String[] HEADERS = {"S.NO", "MODEL", "PURCHASE_DATE", "VIN", "CHASSIS_NUMBER", "REG_NUMBER", "VARIANT(FUEL)",
            "BRANCH", "KM_READING", "FIRST_NAME", "LAST_NAME", "CONTACT_NUMBER", "ALT_CONTACT_NUMBER", "GENDER",
            "EMAIL", "DATE_OF_BIRTH"};

    public VehicleBulkUploadDTO read(InputStream inputStream, final String tenant) {
        VehicleBulkUploadDTO vehicleBulkUploadDTO = new VehicleBulkUploadDTO();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheet(VEHICLE);
            Map<Integer, String> headerColumnIndexAndName = header(sheet);

            Iterator<Row> rows = sheet.iterator();
            final var physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
            int currentRow = 1;
            while (physicalNumberOfRows > currentRow) {
                final var vehicleRequest = forEachRow(tenant, headerColumnIndexAndName, sheet.getRow(currentRow));
                List<ValidationError> errorList = validate(vehicleRequest);
                if (Objects.isNull(errorList) || errorList.isEmpty())
                    vehicleBulkUploadDTO.addVehicle(vehicleRequest);
                else
                    vehicleBulkUploadDTO.addErrors(vehicleRequest.getVehicleDetails().getVehicleRegNumber(),
                            errorList, currentRow);

                currentRow++;
            }
        } catch (IOException e) {
            throw new VehicleServicesException(String.format("Failed to parse Excel file: %s", e.getMessage()));
        }
        return vehicleBulkUploadDTO;
    }

    private List<ValidationError> validate(VehicleRequest vehicleRequest) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<VehicleRequest>> violations = validator.validate(vehicleRequest);

        if (violations != null) {
            return violations.stream().map(this::toValidationError).collect(Collectors.toList());
        }

        return null;
    }

    private ValidationError toValidationError(ConstraintViolation<VehicleRequest> violation) {
        return ValidationError.builder()
                .error(violation.getMessage())
                .field(violation.getPropertyPath().toString()).build();
    }

    public Map<Integer, String> header(Sheet sheet) {
        Map<Integer, String> headerColumnIndexAndName = new HashMap<>();
        Row headerRow = sheet.getRow(0);

        final Iterator<Cell> cellIterator = headerRow.cellIterator();
        while (cellIterator.hasNext()) {
            Cell headerCell = cellIterator.next();
            headerColumnIndexAndName.put(headerCell.getColumnIndex(), value(headerCell));
        }
        return headerColumnIndexAndName;
    }

    public VehicleRequest forEachRow(String tenant, Map<Integer, String> headerColumnIndexAndName, Row currentRow) {
        Iterator<Cell> cellsInRow = currentRow.iterator();

        final var vehicleDetailsBuilder = VehicleDetails.builder();
        final var customerRequestBuilder = CustomerRequest.builder();

        while (cellsInRow.hasNext()) {
            Cell currentCell = cellsInRow.next();
            String columnName = headerColumnIndexAndName.get(currentCell.getColumnIndex());
            vehicleDetails(tenant, vehicleDetailsBuilder, currentCell, columnName);
            customer(customerRequestBuilder, currentCell, columnName);
        }
        return VehicleRequest.builder()
                .vehicleDetails(vehicleDetailsBuilder.build())
                .customer(customerRequestBuilder.build())
                .build();
    }

    public void customer(CustomerRequest.CustomerRequestBuilder customerRequestBuilder, Cell currentCell,
                         final String columnName) {
        switch (columnName) {
            case "FIRST_NAME":
                customerRequestBuilder.firstName(value(currentCell));
                return;
            case "LAST_NAME":
                customerRequestBuilder.lastName(value(currentCell));
                break;
            case "CONTACT_NUMBER":
                customerRequestBuilder.contactNumber(value(currentCell));
                break;
            case "ALT_CONTACT_NUMBER":
                customerRequestBuilder.alternateContactNumber(value(currentCell));
                break;
            case "GENDER":
                customerRequestBuilder.gender(toGender(value(currentCell)));
                break;
            case "EMAIL":
                customerRequestBuilder.email(value(currentCell));
                break;
        }
    }

    private Gender toGender(String stringCellValue) {
        return null;
    }

    public void vehicleDetails(String tenant, VehicleDetails.VehicleDetailsBuilder vehicleDetailsBuilder,
                               Cell currentCell, final String columnName) {
        switch (columnName) {
            case "MODEL":
                vehicleDetailsBuilder.vehicleModel(value(currentCell));
                return;
            case "REG_NUMBER":
                vehicleDetailsBuilder.vehicleRegNumber(value(currentCell));
                return;
            case "PURCHASE_DATE":
                vehicleDetailsBuilder.purchaseDate(toLocalDate(DATA_FORMATTER.formatCellValue(currentCell)));
                break;
            case "VIN":
                vehicleDetailsBuilder.vin(value(currentCell));
                break;
            case "CHASSIS_NUMBER":
                vehicleDetailsBuilder.chassisNumber(value(currentCell));
                break;
            case "VARIANT(FUEL)":
                vehicleDetailsBuilder.fuelType(toVehicleVariant(value(currentCell)));
                break;
            case "BRANCH":
                vehicleDetailsBuilder.vehicleModel(tenant);
                break;
            case "KM_READING":
                vehicleDetailsBuilder.kmReading(Integer.parseInt(value(currentCell)));
                break;
        }
    }

    public String value(Cell currentCell) {
        currentCell.setCellType(CellType.STRING);
        return currentCell.getStringCellValue();

    }

    private FuelType toVehicleVariant(String stringCellValue) {
        return FuelType.valueOf(stringCellValue);
    }

    private LocalDate toLocalDate(String dateCellValue) {

        if (StringUtils.isBlank(dateCellValue))
            return null;

        try {
            final var date = DateUtils.parseDate(dateCellValue, MM_DD_YY);
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (ParseException e) {
            log.error("Exception while parsing the date.");
        }
        return null;
    }


}
