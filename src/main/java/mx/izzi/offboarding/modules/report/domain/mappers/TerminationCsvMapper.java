package mx.izzi.offboarding.modules.report.domain.mappers;

import mx.izzi.offboarding.modules.employees.domain.models.Employee;
import mx.izzi.offboarding.modules.report.domain.models.TerminationCsv;
import mx.izzi.offboarding.modules.terminations.domain.models.AccessBlockRequest;
import mx.izzi.offboarding.modules.users.domain.models.User;

public class TerminationCsvMapper {

    public static TerminationCsv from(AccessBlockRequest accessBlockRequest) {
        User user = accessBlockRequest.getUser();
        Employee employee = accessBlockRequest.getEmployee();

        return TerminationCsv.builder()
                .folio(accessBlockRequest.getFolio())
                .endDate(accessBlockRequest.getEndDate())
                .applicationDate(accessBlockRequest.getApplicationDate())
                .terminationReason(accessBlockRequest.getTerminationReason().getDescription())
                .userIdssff(user.getIdssff().toString())
                .userEmail(user.getEmail())
                .userFullName(user.getName().toUpperCase() + "/" + user.getFirstSurname().toUpperCase() + "/"  + user.getSecondSurname().toUpperCase())
                .userWorkCenter(user.getWorkCenter().getRegion() + ", " + user.getWorkCenter().getLocality() + ", " + user.getWorkCenter().getCompany())
                .employeeIdssff(employee.getIdssff().toString())
                .employeeNumber(employee.getNumber().toString())
                .employeeFullName(employee.getName().toUpperCase() + "/" + employee.getFirstSurname().toUpperCase() + "/" + employee.getSecondSurname().toUpperCase())
                .employeeEmail(employee.getEmail())
                .employeeWorkCenter(employee.getWorkCenter().getRegion() + ", " + employee.getWorkCenter().getLocality() + ", " + employee.getWorkCenter().getCompany())
                .immediateBossIdssff(employee.getImmediateBoos().getIdssff().toString())
                .immediateBossFullName(employee.getImmediateBoos().getName().toUpperCase() + "/" + employee.getImmediateBoos().getFirstSurname().toUpperCase() + "/" + employee.getImmediateBoos().getSecondSurname().toUpperCase())
                .immediateBossWorkCenter(employee.getImmediateBoos().getWorkCenter().getRegion() + ", " + employee.getImmediateBoos().getWorkCenter().getCompany())
                .build();
    }
}
