package mx.izzi.offboarding.modules.employees.domain.models;

import mx.izzi.offboarding.modules.employees.domain.dtos.DetailEmployeeDto;

public class EmployeeMapper {

    public static DetailEmployeeDto from(Employee employee) {
        return DetailEmployeeDto.builder()
                .idssff(employee.getIdssff())
                .number(employee.getNumber())
                .name(employee.getName())
                .firstSurname(employee.getFirstSurname())
                .secondSurname(employee.getSecondSurname())
                .email(employee.getEmail())
                .status(employee.getStatus())
                .jobPositionId(employee.getJobPositionId())
                .jobPosition(employee.getJobPosition())
                .positionId(employee.getPositionId())
                .position(employee.getPosition())
                .build();
    }
}
