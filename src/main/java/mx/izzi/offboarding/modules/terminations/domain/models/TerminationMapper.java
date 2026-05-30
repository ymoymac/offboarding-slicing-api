package mx.izzi.offboarding.modules.terminations.domain.models;

import mx.izzi.offboarding.modules.employees.domain.models.EmployeeMapper;
import mx.izzi.offboarding.modules.terminations.domain.dtos.DetailAccessBlockRequestDto;
import mx.izzi.offboarding.modules.terminations.domain.dtos.DetailTerminationReasonDto;
import mx.izzi.offboarding.modules.terminations.domain.dtos.DetailTerminationTypeDto;
import mx.izzi.offboarding.modules.terminations.domain.entities.AccessBlockRequestEntity;
import mx.izzi.offboarding.modules.terminations.domain.entities.TerminationReasonEntity;
import mx.izzi.offboarding.modules.terminations.domain.entities.TerminationTypeEntity;
import mx.izzi.offboarding.modules.users.domain.entities.UserEntity;
import mx.izzi.offboarding.modules.users.domain.mappers.UserMapper;

public class TerminationMapper {

    public static DetailTerminationTypeDto from(TerminationType terminationType) {
        return DetailTerminationTypeDto.builder()
                .terminationTypeId(terminationType.getTerminationTypeId())
                .type(terminationType.getType())
                .description(terminationType.getDescription())
                .build();
    }

    public static DetailTerminationReasonDto from(TerminationReason terminationReason) {
        return DetailTerminationReasonDto.builder()
                .terminationReasonId(terminationReason.getTerminationReasonId())
                .reason(terminationReason.getReason())
                .description(terminationReason.getDescription())
                .build();
    }

    public static DetailAccessBlockRequestDto from(AccessBlockRequest accessBlockRequest) {
        return DetailAccessBlockRequestDto.builder()
                .requestId(accessBlockRequest.getRequestId())
                .folio(accessBlockRequest.getFolio())
                .endDate(accessBlockRequest.getEndDate())
                .applicationDate(accessBlockRequest.getApplicationDate())
                .user(UserMapper.from(accessBlockRequest.getUser()))
                .immediateBoss(UserMapper.from(accessBlockRequest.getImmediateBoss()))
                .employee(EmployeeMapper.from(accessBlockRequest.getEmployee()))
                .terminationType(TerminationMapper.from(accessBlockRequest.getTerminationType()))
                .terminationReason(TerminationMapper.from(accessBlockRequest.getTerminationReason()))
                .build();
    }

    public static AccessBlockRequestEntity toEntity(AccessBlockRequest accessBlockRequest, UserEntity userEntity, UserEntity immediateBossEntity) {
        return AccessBlockRequestEntity.builder()
                .requestId(accessBlockRequest.getRequestId())
                .folio(accessBlockRequest.getFolio())
                .endDate(accessBlockRequest.getEndDate())
                .applicationDate(accessBlockRequest.getApplicationDate())
                .applicantUser(userEntity)
                .immediateBoss(immediateBossEntity)
                .employee(EmployeeMapper.toEntity(accessBlockRequest.getEmployee()))
                .terminationType(TerminationMapper.toEntity(accessBlockRequest.getTerminationType()))
                .terminationReason(TerminationMapper.toEntity(accessBlockRequest.getTerminationReason()))
                .isActive(accessBlockRequest.getIsActive())
                .createdAt(accessBlockRequest.getCreatedAt())
                .updatedAt(accessBlockRequest.getUpdatedAt())
                .createdBy(accessBlockRequest.getCreatedBy())
                .updatedBy(accessBlockRequest.getUpdatedBy())
                .build();
    }

    public static TerminationTypeEntity toEntity(TerminationType terminationType) {
        return TerminationTypeEntity.builder()
                .terminationTypeId(terminationType.getTerminationTypeId())
                .type(terminationType.getType())
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
                .reason(terminationReason.getReason())
                .description(terminationReason.getDescription())
                .isActive(terminationReason.getIsActive())
                .createdAt(terminationReason.getCreatedAt())
                .updatedAt(terminationReason.getUpdatedAt())
                .createdBy(terminationReason.getCreatedBy())
                .updatedBy(terminationReason.getUpdatedBy())
                .build();
    }
}
