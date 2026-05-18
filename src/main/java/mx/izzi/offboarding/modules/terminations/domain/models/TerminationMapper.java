package mx.izzi.offboarding.modules.terminations.domain.models;

import mx.izzi.offboarding.modules.terminations.domain.dtos.DetailTerminationReasonDto;
import mx.izzi.offboarding.modules.terminations.domain.dtos.DetailTerminationTypeDto;

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
}
