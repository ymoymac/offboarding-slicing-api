package mx.izzi.offboarding.modules.reports.services.implementations;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.RequiredArgsConstructor;
import mx.izzi.offboarding.modules.reports.domain.mappers.TerminationCsvMapper;
import mx.izzi.offboarding.modules.reports.domain.models.TerminationCsv;
import mx.izzi.offboarding.modules.reports.services.CsvReportService;
import mx.izzi.offboarding.modules.terminations.domain.models.AccessBlockRequest;
import mx.izzi.offboarding.modules.terminations.services.TerminationService;
import mx.izzi.offboarding.shared.exceptions.ServerException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CsvReportServiceImpl implements CsvReportService {

    private final String PATH = "/api/v1/reports/download/access-block-request";
    private final TerminationService terminationService;

    @Override
    public byte[] generateCsvReport(String folio) throws IOException {

        Optional<AccessBlockRequest> termination = this.terminationService.findOneBy(folio);

        if (termination.isEmpty()) {
            throw new ServerException(PATH + "/" + folio);
        }

        List<TerminationCsv> rows = Stream.of(termination.get())
                .map(TerminationCsvMapper::from)
                .toList();


        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            OutputStreamWriter osw = new OutputStreamWriter(baos);

            baos.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

            ColumnPositionMappingStrategy<TerminationCsv> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(TerminationCsv.class);

            String[] headers = {
                    "FOLIO",
                    "FECHA DE BAJA",
                    "FECHA DE CAPTURA",
                    "MOTIVO DE BAJA",
                    "IDSSFF DEL SOLICITANTE",
                    "CORREO DEL SOLICITANTE",
                    "NOMBRE DEL SOLICITANTE",
                    "EMPRESA DEL SOLICITANTE",
                    "IDSSFF DEL EMPLEADO",
                    "NO. DEL EMPLEADO",
                    "NOMBRE DEL EMPLEADO",
                    "CORREO DEL EMPLEADO",
                    "EMPRESA DEL EMPLEADO",
                    "IDSSFF DEL JEFE INMEDIATO",
                    "NOMBRE DEL JEFE INMEDIATO",
                    "EMPRESA DEL JEFE INMEDIATO",
            };
            strategy.setColumnMapping(headers);

            osw.write(String.join(",", headers) + "\n");

            new StatefulBeanToCsvBuilder<TerminationCsv>(osw)
                    .withMappingStrategy(strategy)
                    .withApplyQuotesToAll(false)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build()
                    .write(rows);

            osw.flush();
            return baos.toByteArray();

        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            throw new ServerException(PATH + "/" + folio);
        }
    }
}
