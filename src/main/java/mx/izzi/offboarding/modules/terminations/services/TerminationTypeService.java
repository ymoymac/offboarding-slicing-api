package mx.izzi.offboarding.modules.terminations.services;

import mx.izzi.offboarding.modules.terminations.domain.models.TerminationType;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface TerminationTypeService {
    Optional<TerminationType> findOneBy(Long terminationTypeId);
    Page<TerminationType> findAll(int page, int size);
}
