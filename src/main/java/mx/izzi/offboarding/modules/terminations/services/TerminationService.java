package mx.izzi.offboarding.modules.terminations.services;

import mx.izzi.offboarding.modules.terminations.domain.dtos.CreateAccessBlockRequestDto;
import mx.izzi.offboarding.modules.terminations.domain.models.AccessBlockRequest;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface TerminationService {
    Optional<AccessBlockRequest> findOneBy(String folio);
    Page<AccessBlockRequest> findAllTerminationsByUserId(Long userIdssff, int page, int size);
    Optional<AccessBlockRequest> create(CreateAccessBlockRequestDto dto);
}
