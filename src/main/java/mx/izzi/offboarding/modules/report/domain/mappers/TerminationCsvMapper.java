package mx.izzi.offboarding.modules.report.domain.mappers;

import mx.izzi.offboarding.modules.employees.domain.models.Employee;
import mx.izzi.offboarding.modules.report.domain.models.TerminationCsv;
import mx.izzi.offboarding.modules.terminations.domain.models.AccessBlockingRequest;
import mx.izzi.offboarding.modules.users.domain.models.User;

public class TerminationCsvMapper {

    public static TerminationCsv from(AccessBlockingRequest accessBlockingRequest) {
        User user = accessBlockingRequest.getUser();
        Employee employee = accessBlockingRequest.getEmployee();

        return TerminationCsv.builder()
                .folio(accessBlockingRequest.getFolio())
                .endDate(accessBlockingRequest.getEndDate())
                .applicationDate(accessBlockingRequest.getApplicationDate())
                .terminationReason(accessBlockingRequest.getTerminationReason().getDescription())
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
