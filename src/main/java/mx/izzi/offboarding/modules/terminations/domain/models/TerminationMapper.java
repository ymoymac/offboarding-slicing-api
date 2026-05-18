package mx.izzi.offboarding.modules.terminations.domain.models;

import mx.izzi.offboarding.modules.employees.domain.models.EmployeeMapper;
import mx.izzi.offboarding.modules.terminations.domain.dtos.DetailAccessBlockingRequestDto;
import mx.izzi.offboarding.modules.terminations.domain.dtos.DetailTerminationReasonDto;
import mx.izzi.offboarding.modules.terminations.domain.dtos.DetailTerminationTypeDto;
import mx.izzi.offboarding.modules.terminations.domain.entities.AccessBlockingRequestEntity;
import mx.izzi.offboarding.modules.terminations.domain.entities.TerminationReasonEntity;
import mx.izzi.offboarding.modules.terminations.domain.entities.TerminationTypeEntity;
import mx.izzi.offboarding.modules.users.domain.models.UserMapper;

public class TerminationMapper {

    public static DetailTerminationTypeDto from(TerminationType terminationType) {
        return DetailTerminationTypeDto.builder()
                .terminationTypeId(terminationType.getTerminationTypeId())
                .terminationType(terminationType.getTerminationType())
                .description(terminationType.getDescription())
                .build();
    }

    public static DetailTerminationReasonDto from(TerminationReason terminationReason) {
        return DetailTerminationReasonDto.builder()
                .terminationReasonId(terminationReason.getTerminationReasonId())
                .terminationReason(terminationReason.getTerminationReason())
                .description(terminationReason.getDescription())
                .build();
    }

    public static DetailAccessBlockingRequestDto from(AccessBlockingRequest accessBlockingRequest) {
        return DetailAccessBlockingRequestDto.builder()
                .requestId(accessBlockingRequest.getRequestId())
                .folio(accessBlockingRequest.getFolio())
                .endDate(accessBlockingRequest.getEndDate())
                .applicationDate(accessBlockingRequest.getApplicationDate())
                .laboraId(accessBlockingRequest.getLaboraId())
                .user(UserMapper.from(accessBlockingRequest.getUser()))
                .employee(EmployeeMapper.from(accessBlockingRequest.getEmployee()))
                .terminationType(TerminationMapper.from(accessBlockingRequest.getTerminationType()))
                .terminationReason(TerminationMapper.from(accessBlockingRequest.getTerminationReason()))
                .build();
    }

    public static AccessBlockingRequestEntity toEntity(AccessBlockingRequest accessBlockingRequest) {
        return AccessBlockingRequestEntity.builder()
                .requestId(accessBlockingRequest.getRequestId())
                .folio(accessBlockingRequest.getFolio())
                .endDate(accessBlockingRequest.getEndDate())
                .applicationDate(accessBlockingRequest.getApplicationDate())
                .laboraId(accessBlockingRequest.getLaboraId())
                .user(UserMapper.toEntity(accessBlockingRequest.getUser()))
                .employee(EmployeeMapper.toEntity(accessBlockingRequest.getEmployee()))
                .terminationType(TerminationMapper.toEntity(accessBlockingRequest.getTerminationType()))
                .terminationReason(TerminationMapper.toEntity(accessBlockingRequest.getTerminationReason()))
                .isActive(accessBlockingRequest.getIsActive())
                .createdAt(accessBlockingRequest.getCreatedAt())
                .updatedAt(accessBlockingRequest.getUpdatedAt())
                .createdBy(accessBlockingRequest.getCreatedBy())
                .updatedBy(accessBlockingRequest.getUpdatedBy())
                .build();
    }

    public static TerminationTypeEntity toEntity(TerminationType terminationType) {
        return TerminationTypeEntity.builder()
                .terminationTypeId(terminationType.getTerminationTypeId())
                .terminationType(terminationType.getTerminationType())
                .description(terminationType.getDescription())
                .isActive(terminationType.getIsActive())
                .createdAt(terminationType.getCreatedAt())
                .updatedAt(terminationType.getUpdatedAt())
                .createdBy(terminationType.getCreatedBy())
                .updatedBy(terminationType.getUpdatedBy())
                .build();
    }

    public static TerminationReasonEntity toEntity(TerminationReason terminationReason) {
        return TerminationReasonEntity.builder()
                .terminationReasonId(terminationReason.getTerminationReasonId())
                .terminationReason(terminationReason.getTerminationReason())
                .description(terminationReason.getDescription())
                .isActive(terminationReason.getIsActive())
                .createdAt(terminationReason.getCreatedAt())
                .updatedAt(terminationReason.getUpdatedAt())
                .createdBy(terminationReason.getCreatedBy())
                .updatedBy(terminationReason.getUpdatedBy())
                .build();
    }
}
