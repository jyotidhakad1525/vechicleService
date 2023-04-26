package com.automate.vehicleservices.service;

import com.automate.vehicleservices.entity.RoDataDetails;
import com.automate.vehicleservices.framework.exception.VehicleServicesException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RoDataExcelReader extends AbstractExcelReaderService {

    public static final String RO_DATA_TEMPLETE = "RO data Templete";


    @Override
    public List<RoDataDetails> read(InputStream inputStream, final String tenant) {
        List<RoDataDetails> roDataDTOList = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheet(RO_DATA_TEMPLETE);
            Map<Integer, String> headerColumnIndexAndName = header(sheet);

            final var physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
            int currentRow = 1;
            while (physicalNumberOfRows > currentRow) {
                final var roData = forEachRow(tenant, headerColumnIndexAndName, sheet.getRow(currentRow));
                roDataDTOList.add(roData);
                currentRow++;
            }
        } catch (IOException e) {
            throw new VehicleServicesException(String.format("Failed to parse Excel file: %s", e.getMessage()));
        }
        return roDataDTOList;
    }

    public RoDataDetails forEachRow(String tenant, Map<Integer, String> headerColumnIndexAndName, Row currentRow) {
        Iterator<Cell> cellsInRow = currentRow.iterator();

        final var roDataBuilder = RoDataDetails.builder();

        while (cellsInRow.hasNext()) {
            Cell currentCell = cellsInRow.next();
            String columnName = headerColumnIndexAndName.get(currentCell.getColumnIndex());
            roDetails(roDataBuilder, currentCell, columnName);
        }
        return roDataBuilder.build();
    }

    public void roDetails(RoDataDetails.RoDataDetailsBuilder roData, Cell currentCell,
                          final String columnName) {
        switch (columnName) {
            case "Model":
                roData.model(value(currentCell));
                break;
            case "Vehicle Reg No":
                roData.vehicleRegNo(value(currentCell));
                break;
            case "VIN":
                roData.vin(value(currentCell));
                break;
            case "Service Center Location":
                roData.serviceCenterLocation(value(currentCell));
                break;
            case "Service Center Code":
            case "Cervice Center Code":
                roData.serviceCenterCode(value(currentCell));
                break;
            case "Bill No":
                roData.billNo(value(currentCell));
                break;
            case "Bill Date":
                roData.billDate(value(currentCell));
                break;
            case "Bill Type":
                roData.billType(value(currentCell));
                break;
            case "Customer Name":
                roData.customerName(value(currentCell));
                break;
            case "Mobile No":
                roData.mobileNumber(value(currentCell));
                break;
            case "R/O No":
                roData.roNumber(value(currentCell));
                break;
            case "Technician":
                roData.technician(value(currentCell));
                break;
            case "Service Advisor":
                roData.serviceAdvisor(value(currentCell));
                break;
            case "Work Type":
                roData.serviceType(value(currentCell));
                break;
            case "R/O Date":
                roData.roDate(value(currentCell));
                break;
            case "Total Bill Amount":
                roData.totalBillAmount(doubleValue(currentCell));
                break;
            case "Labour Amt":
                roData.labourAmount(doubleValue(currentCell));
                break;
            case "Labour Tax":
                roData.labourTax(doubleValue(currentCell));
                break;
            case "Part Amt":
                roData.partAmount(doubleValue(currentCell));
                break;
            case "Part Tax":
                roData.partTax(doubleValue(currentCell));
                break;


        }
    }


}
