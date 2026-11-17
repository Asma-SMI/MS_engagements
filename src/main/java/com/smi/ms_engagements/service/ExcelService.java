package com.smi.ms_engagements.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {
    public byte[] generateExcel(
            List<Map<String, Object>> data,
            String sheetName) throws IOException {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream output =
                     new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(sheetName);

            CellStyle headerStyle = workbook.createCellStyle();

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);

            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(
                    IndexedColors.DARK_BLUE.getIndex()
            );
            headerStyle.setFillPattern(
                    FillPatternType.SOLID_FOREGROUND
            );
            headerFont.setColor(
                    IndexedColors.WHITE.getIndex()
            );

            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(
                    workbook.getCreationHelper()
                            .createDataFormat()
                            .getFormat("dd/mm/yyyy")
            );

            if (data.isEmpty()) {
                Row row = sheet.createRow(0);
                row.createCell(0)
                        .setCellValue("Aucune donnee pour cette date d'arrete");

                sheet.setColumnWidth(0, 14000);

            } else {

                List<String> columns =
                        new ArrayList<>(data.get(0).keySet());

                Row headerRow = sheet.createRow(0);

                for (int col = 0; col < columns.size(); col++) {

                    Cell cell = headerRow.createCell(col);
                    cell.setCellValue(columns.get(col));
                    cell.setCellStyle(headerStyle);

                    sheet.setColumnWidth(col, 5500);
                }

                int rowIndex = 1;

                for (Map<String, Object> record : data) {

                    Row row = sheet.createRow(rowIndex++);

                    for (int col = 0; col < columns.size(); col++) {

                        Object value = record.get(columns.get(col));
                        Cell cell = row.createCell(col);

                        if (value == null) {
                            continue;
                        }

                        if (value instanceof BigDecimal number) {
                            cell.setCellValue(number.doubleValue());

                        } else if (value instanceof Number number) {
                            cell.setCellValue(number.doubleValue());

                        } else if (value instanceof java.sql.Date date) {
                            cell.setCellValue(date);
                            cell.setCellStyle(dateStyle);

                        } else if (value instanceof Timestamp timestamp) {
                            cell.setCellValue(timestamp);
                            cell.setCellStyle(dateStyle);

                        } else if (value instanceof LocalDate date) {
                            cell.setCellValue(date);
                            cell.setCellStyle(dateStyle);

                        } else if (value instanceof LocalDateTime dateTime) {
                            cell.setCellValue(dateTime);
                            cell.setCellStyle(dateStyle);

                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                }

                sheet.createFreezePane(0, 1);
                sheet.setAutoFilter(
                        new org.apache.poi.ss.util.CellRangeAddress(
                                0, data.size(), 0, columns.size() - 1
                        )
                );
            }

            workbook.write(output);
            return output.toByteArray();
        }
    }
}
