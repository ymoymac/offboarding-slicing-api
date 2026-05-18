package mx.izzi.offboarding.modules.terminations.repositories;

import mx.izzi.offboarding.modules.terminations.domain.entities.TerminationReasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerminationReasonRepository extends JpaRepository<TerminationReasonEntity, Long> {
}
