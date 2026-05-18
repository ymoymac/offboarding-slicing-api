package mx.izzi.offboarding.modules.report.services;

import java.io.IOException;

public interface CsvReportService {
    byte[] generateCsvReport(String folio) throws IOException;
}
