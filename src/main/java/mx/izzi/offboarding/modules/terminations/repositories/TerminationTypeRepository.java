package mx.izzi.offboarding.modules.terminations.repositories;

import mx.izzi.offboarding.modules.terminations.domain.entities.TerminationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerminationTypeRepository extends JpaRepository<TerminationTypeEntity, Long> {
}
