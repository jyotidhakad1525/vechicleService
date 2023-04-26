package com.automate.vehicleservices.service;

import com.automate.vehicleservices.entity.enums.FuelType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.InputStream;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public abstract class AbstractExcelReaderService {
    private static final String MM_DD_YY = "MM/dd/yy";

    protected Map<Integer, String> header(Sheet sheet) {
        Map<Integer, String> headerColumnIndexAndName = new HashMap<>();
        Row headerRow = sheet.getRow(0);

        final Iterator<Cell> cellIterator = headerRow.cellIterator();
        while (cellIterator.hasNext()) {
            Cell headerCell = cellIterator.next();
            headerColumnIndexAndName.put(headerCell.getColumnIndex(), value(headerCell));
        }
        return headerColumnIndexAndName;
    }

    protected String value(Cell currentCell) {
        currentCell.setCellType(CellType.STRING);
        return currentCell.getStringCellValue();
    }

    protected Double doubleValue(Cell currentCell) {
        currentCell.setCellType(CellType.NUMERIC);
        return currentCell.getNumericCellValue();
    }

    protected LocalDate toLocalDate(String dateCellValue) {

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

    protected FuelType toVehicleVariant(String stringCellValue) {
        return FuelType.valueOf(stringCellValue);
    }

    protected abstract <T> T read(InputStream inputStream, final String tenant);


}
