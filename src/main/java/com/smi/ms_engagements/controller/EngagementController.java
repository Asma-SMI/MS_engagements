
package com.smi.ms_engagements.controller;

import com.smi.ms_engagements.service.EngagementService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

@RestController
@RequestMapping("/api/excel")
public class EngagementController {

    private final EngagementService engagementService;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    private static final DateTimeFormatter FILE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    public EngagementController(EngagementService engagementService) {
        this.engagementService = engagementService;
    }

    @GetMapping("/cdi")
    public ResponseEntity<byte[]> exportCdi(
            @RequestParam String dateArrete) throws IOException {

        LocalDate date = LocalDate.parse(dateArrete, DATE_FORMAT);

        byte[] excel = engagementService.generateCdiExcel(date);

        String filename = "ENGAGEMENT_CDI_"
                + date.format(FILE_FORMAT) + ".xlsx";

        return downloadExcel(excel, filename);
    }

    @GetMapping("/cde")
    public ResponseEntity<byte[]> exportCde(
            @RequestParam String dateArrete) throws IOException {

        LocalDate date = LocalDate.parse(dateArrete, DATE_FORMAT);

        byte[] excel = engagementService.generateCdeExcel(date);

        String filename = "ENGAGEMENT_CDE_"
                + date.format(FILE_FORMAT) + ".xlsx";

        return downloadExcel(excel, filename);
    }

    private ResponseEntity<byte[]> downloadExcel(
            byte[] excel, String filename) {

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\""
                )
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument."
                                + "spreadsheetml.sheet"
                ))
                .contentLength(excel.length)
                .body(excel);
    }
}
