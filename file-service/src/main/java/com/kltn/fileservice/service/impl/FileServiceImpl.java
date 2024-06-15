package com.kltn.fileservice.service.impl;

import com.kltn.fileservice.dto.ObjectFileDTO;
import com.kltn.fileservice.service.FileService;
import com.kltn.fileservice.service.MinioService;
import com.kltn.fileservice.utils.Constants;
import com.kltn.fileservice.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);
    private final MinioService minioService;

    @Override
    public List<ObjectFileDTO> uploadFiles(String tenant, String channel, MultipartFile[] files) {
        List<ObjectFileDTO> result = new ArrayList<>();
        String rootPath = minioService.getRootFolder(channel);
        for (MultipartFile file : files) {
            try {
                String filePath = rootPath + File.separator + UUID.randomUUID() + "_" + file.getOriginalFilename();
                minioService.uploadFile(tenant, filePath, file.getBytes());
                ObjectFileDTO objectFileDTO = ObjectFileDTO.builder()
                        .linkUrl(Constants.GET_CONTENT_LINK_URL)
                        .linkUrlPublic(Constants.GET_CONTENT_LINK_URL_PUBLIC)
                        .fileName(file.getOriginalFilename())
                        .filePath(Utils.encrypt(filePath))
                        .fileSize(file.getSize())
                        .build();
                result.add(objectFileDTO);
            } catch (Exception e) {
                LOGGER.error("Error uploadFiles: ", e);
            }
        }
        return result;
    }


    @Override
    public byte[] getFile(String tenant, String filePath) {
        try {
            return minioService.getFile(tenant, Utils.decrypt(filePath));
        } catch (Exception e) {
            LOGGER.error("Error getFile: ", e);
            return null;
        }
    }

    @Override
    public List<List<Object>> convertFileToJson(MultipartFile file) {
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