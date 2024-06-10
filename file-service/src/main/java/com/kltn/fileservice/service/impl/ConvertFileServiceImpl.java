package com.kltn.fileservice.service.impl;

import com.kltn.fileservice.constants.UserType;
import com.kltn.fileservice.service.ConvertFileService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConvertFileServiceImpl implements ConvertFileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertFileServiceImpl.class);

    @Override
    public List<List<Object>> convertFileToJson(MultipartFile file, UserType type) {
        List<List<Object>> excelData = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                List<Object> rowData = new ArrayList<>();

                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    rowData.add(cell == null ? null : cell.toString());
                }
                excelData.add(rowData);
            }
        } catch (IOException e) {
            LOGGER.error("Error reading file", e);
        }

        return excelData;
    }
}