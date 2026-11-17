package com.smi.ms_engagements.service;

import com.smi.ms_engagements.repository.EngagementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class EngagementService {
    private final EngagementRepository repository;
    private final ExcelService excelService;

    public EngagementService(
            EngagementRepository repository,
            ExcelService excelService) {

        this.repository = repository;
        this.excelService = excelService;
    }

    @Transactional(readOnly = true)
    public byte[] generateCdiExcel(LocalDate dateArrete)
            throws IOException {

        List<Map<String, Object>> resultats =
                repository.extractCdi(dateArrete);

        return excelService.generateExcel(
                resultats,
                "Engagements CDI"
        );
    }

    @Transactional(readOnly = true)
    public byte[] generateCdeExcel(LocalDate dateArrete)
            throws IOException {

        List<Map<String, Object>> resultats =
                repository.extractCde(dateArrete);

        return excelService.generateExcel(
                resultats,
                "Engagements CDE"
        );
    }
}
