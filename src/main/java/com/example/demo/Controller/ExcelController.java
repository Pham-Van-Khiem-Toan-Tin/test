package com.example.demo.Controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@RestController
@RequestMapping("/export")
public class ExcelController {

    @GetMapping("/excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        // Tạo đường dẫn tới tệp Excel
        String filePath = "C:/demo/file.xlsx";

        // Kiểm tra xem tệp đã tồn tại hay chưa
        File file = new File(filePath);
        boolean fileExists = file.exists();

        // Nếu tệp chưa tồn tại, tạo tệp mới
        if (!fileExists) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        // Tạo một workbook Excel mới hoặc đọc workbook từ tệp đã tồn tại
        Workbook workbook;
        if (fileExists) {
            FileInputStream inputStream = new FileInputStream(file);
            workbook = new XSSFWorkbook(inputStream);
        } else {
            workbook = new XSSFWorkbook();
        }

        // Tạo một trang tính trong workbook
        Sheet sheet = workbook.createSheet("Data1");

        // Tạo dữ liệu mẫu
        String[] columnHeaders  = {"Name", "Email", "Phone"};
        String[][] data = {
                {"John Doe", "john@example.com", "123456789"},
                {"Jane Smith", "jane@example.com", "987654321"}
        };

        // Tạo header
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columnHeaders .length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
        }

        // Tạo dữ liệu
        int rowNum = sheet.getLastRowNum();
        for (int i = 0; i < data.length; i++) {
            Row dataRow = sheet.createRow(rowNum + 1 + i);
            for (int j = 0; j < data[i].length; j++) {
                Cell cell = dataRow.createCell(j);
                cell.setCellValue(data[i][j]);
            }
        }

        // Ghi workbook vào OutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        // Ghi outputStream vào tệp Excel
        Files.write(Path.of(filePath), outputStream.toByteArray(), StandardOpenOption.WRITE);

        // Thiết lập các thông tin header và trả về tệp Excel
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.xlsx");

        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }
}


