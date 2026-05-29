package mx.izzi.offboarding.modules.reports.domain.models;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Data
public class TerminationCsv {

    @CsvBindByPosition(position = 0)
    private String folio;

    @CsvBindByPosition(position = 1)
    private Date endDate;

    @CsvBindByPosition(position = 2)
    private LocalDateTime applicationDate;

    @CsvBindByPosition(position = 3)
    private String terminationReason;

    //User
    @CsvBindByPosition(position = 4)
    private String userIdssff;

    @CsvBindByPosition(position = 5)
    private String userEmail;

    @CsvBindByPosition(position = 6)
    private String userFullName;

    @CsvBindByPosition(position = 7)
    private String userWorkCenter;

    //Employee
    @CsvBindByPosition(position = 8)
    private String employeeIdssff;

    @CsvBindByPosition(position = 9)
    private String employeeNumber;

    @CsvBindByPosition(position = 10)
    private String employeeFullName;

    @CsvBindByPosition(position = 11)
    private String employeeEmail;

    @CsvBindByPosition(position = 12)
    private String employeeWorkCenter;

    @CsvBindByPosition(position = 13)
    private String immediateBossIdssff;

    @CsvBindByPosition(position = 14)
    private String immediateBossFullName;

    @CsvBindByPosition(position = 15)
    private String immediateBossWorkCenter;
}
