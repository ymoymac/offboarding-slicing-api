package mx.izzi.offboarding.modules.terminations.services;

import mx.izzi.offboarding.modules.terminations.domain.dtos.CreateAccessBlockingRequestDto;
import mx.izzi.offboarding.modules.terminations.domain.models.AccessBlockingRequest;

import java.util.Optional;

public interface TerminationService {
    Optional<AccessBlockingRequest> findOneBy(String folio);
    Optional<AccessBlockingRequest> create(CreateAccessBlockingRequestDto dto);
}
