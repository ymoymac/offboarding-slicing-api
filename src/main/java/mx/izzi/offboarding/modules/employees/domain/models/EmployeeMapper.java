package mx.izzi.offboarding.modules.employees.domain.models;

import mx.izzi.offboarding.modules.employees.domain.dtos.DetailEmployeeDto;
import mx.izzi.offboarding.modules.employees.domain.dtos.UpdateEmployeeDto;
import mx.izzi.offboarding.modules.employees.domain.entities.EmployeeEntity;
import mx.izzi.offboarding.modules.users.domain.models.UserMapper;
import mx.izzi.offboarding.modules.workcenter.domain.models.WorkCenterMapper;

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
                .workCenter(WorkCenterMapper.from(employee.getWorkCenter()))
                .build();
    }

    public static UpdateEmployeeDto toDto(Employee employee) {
        return UpdateEmployeeDto.builder()
                .number(employee.getNumber())
                .email(employee.getEmail())
                .status(employee.getStatus())
                .jobPositionId(employee.getJobPositionId())
                .jobPosition(employee.getJobPosition())
                .positionId(employee.getPositionId())
                .position(employee.getPosition())
                .workCenterId(Long.parseLong(employee.getWorkCenter().getWorkCenterId()))
                .build();
    }

    public static EmployeeEntity toEntity(Employee employee) {
        return EmployeeEntity.builder()
                .employeeId(employee.getEmployeeId())
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
                .isActive(employee.getIsActive())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .createdBy(employee.getCreatedBy())
                .updatedBy(employee.getUpdatedBy())
                .immediateBoss(UserMapper.toEntity(employee.getImmediateBoos()))
                .workCenter(WorkCenterMapper.toEntity(employee.getWorkCenter()))
                .build();
    }
}
