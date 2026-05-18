package mx.izzi.offboarding.modules.report.controllers;

import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.report.services.CsvReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/v1/reports")
@RequiredArgsConstructor
public class CsvReportController {

    private final CsvReportService csvReportService;

    @GetMapping(value = "/download/access-block-request/{folio}", produces = "text/csv")
    public ResponseEntity<byte[]> exportCsv(@PathVariable String folio) throws IOException {
        byte[] csv = this.csvReportService.generateCsvReport(folio);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reporte.csv\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(csv);
    }
}
