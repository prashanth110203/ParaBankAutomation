package com.parabank.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelReader {
    
    private Workbook workbook;
    private Sheet sheet;
    
    public ExcelReader(String excelFilePath, String sheetName) {
        try {
            File file = new File(excelFilePath);
            
            if (!file.exists()) {
                String absolutePath = file.getAbsolutePath();
                throw new RuntimeException("Excel file not found at: " + absolutePath);
            }
            
            FileInputStream fis = new FileInputStream(file);
            workbook = new XSSFWorkbook(fis);
            
            System.out.println("Excel file loaded successfully");
            System.out.println("Available sheets in Excel:");
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                System.out.println("  - Sheet " + (i+1) + ": " + workbook.getSheetName(i));
            }
            
            sheet = workbook.getSheet(sheetName);
            
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in Excel file. Please check the sheet name.");
            }
            
            System.out.println("Successfully loaded sheet: " + sheetName);
            System.out.println("Total rows in sheet: " + (sheet.getLastRowNum() + 1));
            
        } catch (IOException e) {
            throw new RuntimeException("Error loading Excel file: " + e.getMessage(), e);
        }
    }
    
    public List<Map<String, String>> getAllRowsAsMaps() {
        List<Map<String, String>> dataList = new ArrayList<>();
        
        if (sheet == null) {
            throw new RuntimeException("Sheet is null. Excel file may not have been loaded correctly.");
        }
        
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new RuntimeException("Header row (first row) is empty in Excel sheet");
        }
        
        int columnCount = headerRow.getLastCellNum();
        System.out.println("Number of columns found: " + columnCount);
        
        List<String> columnNames = new ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                String columnName = cell.getStringCellValue().trim();
                columnNames.add(columnName);
                System.out.println("  Column " + (i+1) + ": " + columnName);
            }
        }
        
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;
            
            Map<String, String> rowData = new HashMap<>();
            boolean hasData = false;
            
            for (int colIndex = 0; colIndex < columnCount; colIndex++) {
                Cell cell = row.getCell(colIndex);
                String columnName = columnNames.get(colIndex);
                String cellValue = getCellValueAsString(cell);
                
                if (cellValue != null && !cellValue.trim().isEmpty()) {
                    hasData = true;
                }
                
                rowData.put(columnName, cellValue);
            }
            
            if (hasData) {
                dataList.add(rowData);
                System.out.println("Loaded row " + rowIndex + ": " + rowData.get("FirstName") + " " + rowData.get("LastName"));
            }
        }
        
        System.out.println("Total data rows loaded: " + dataList.size());
        return dataList;
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue().trim();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString();
                    } else {
                        double value = cell.getNumericCellValue();
                        if (value == Math.floor(value)) {
                            return String.valueOf((long) value);
                        }
                        return String.valueOf(value);
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    return cell.getCellFormula();
                case BLANK:
                    return "";
                default:
                    return "";
            }
        } catch (Exception e) {
            System.err.println("Error reading cell: " + e.getMessage());
            return "";
        }
    }
    
    public void close() {
        try {
            if (workbook != null) {
                workbook.close();
                System.out.println("Excel workbook closed");
            }
        } catch (IOException e) {
            System.err.println("Error closing workbook: " + e.getMessage());
        }
    }
}