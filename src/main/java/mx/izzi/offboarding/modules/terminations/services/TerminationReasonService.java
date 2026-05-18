package mx.izzi.offboarding.modules.terminations.services;

import mx.izzi.offboarding.modules.terminations.domain.models.TerminationReason;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface TerminationReasonService {
    Optional<TerminationReason> findOneBy(Long terminationReasonId);
    Page<TerminationReason> findAll(int page, int size);
}
